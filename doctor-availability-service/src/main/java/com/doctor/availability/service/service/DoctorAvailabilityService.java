package com.doctor.availability.service.service;

import com.doctor.availability.service.dto.AppointmentData;
import com.doctor.availability.service.dto.AppointmentDto;
import com.doctor.availability.service.dto.DoctorAvailabilityDto;
import com.doctor.availability.service.entity.DoctorAvailability;
import com.doctor.availability.service.producer.AppointmentCancelledProducer;
import com.doctor.availability.service.producer.ReservationConfirmedProducer;
import com.doctor.availability.service.repository.DoctorAvailabilityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorAvailabilityService {
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final ReservationConfirmedProducer reservationConfirmedProducer;
    private final AppointmentCancelledProducer appointmentCancelledProducer;

    @RabbitListener(
            bindings =
            @QueueBinding(
                    value = @Queue(value = "appointment.created.request"),
                    exchange = @Exchange(value = "appointment.exchange"),
                    key = "appointment.created"
            )
    )
    public void checkDoctorAvailability(AppointmentData appointmentData) throws JsonProcessingException {
        String appointmentDate = appointmentData.getAppointmentDto().getAppointmentDate();  // yyyy-MM-dd
        String appointmentTime = appointmentData.getAppointmentDto().getAppointmentTime();  // HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime requestedTime = LocalDateTime.parse(appointmentDate + " " + appointmentTime, formatter);
        DoctorAvailability doctorAvailability = doctorAvailabilityRepository.findAvailableSlot(appointmentData.getAppointmentDto().getDoctorId(), requestedTime);

        try {
            if (Objects.nonNull(doctorAvailability)) {
                doctorAvailability.setIsBooked(true);
                DoctorAvailability updateDoctorAvailabilityDto = doctorAvailabilityRepository.save(doctorAvailability);
                appointmentData.setDoctorAvailabilityDto(new DoctorAvailabilityDto(updateDoctorAvailabilityDto.getId(),updateDoctorAvailabilityDto.getIsBooked()));
                log.info("Doctor is available, booking the slot and confirming reservation for appointment: {}", appointmentData);
                reservationConfirmedProducer.appointmentCreate(appointmentData);
            } else {
                log.info("No available slot found for doctorId: {} at requested time: {}", appointmentData.getAppointmentDto().getDoctorId(), requestedTime);
                appointmentCancelledProducer.appointmentCancelled(appointmentData);
            }
        } catch (Exception e) {
            if(Objects.nonNull(doctorAvailability)){
                doctorAvailability.setIsBooked(false);
                doctorAvailabilityRepository.save(doctorAvailability);
            }
            log.error("Error processing doctor availability, cancelling appointment for doctorId: {} ", appointmentData.getAppointmentDto().getDoctorId());
            appointmentCancelledProducer.appointmentCancelled(appointmentData);
        }
    }

    @RabbitListener(
            bindings =
            @QueueBinding(
                    value = @Queue(value = "reservation.cancelled.request"),
                    exchange = @Exchange(value = "reservation.exchange"),
                    key = "reservation.cancelled"
            )
    )
    public void reservationCancelled(AppointmentData appointmentData) throws JsonProcessingException {
        if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getDoctorAvailabilityDto().getId())) {
            Optional<DoctorAvailability> doctorAvailability = doctorAvailabilityRepository.findById(appointmentData.getDoctorAvailabilityDto().getId());
            if(doctorAvailability.isPresent()){
                doctorAvailability.get().setIsBooked(false);
                doctorAvailabilityRepository.save(doctorAvailability.get());
            }
        }
        log.error("Error processing doctor availability, cancelling appointment for doctorId: {} ", appointmentData.getAppointmentDto().getDoctorId());
        appointmentCancelledProducer.appointmentCancelled(appointmentData);
    }
}
