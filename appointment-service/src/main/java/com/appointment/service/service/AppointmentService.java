package com.appointment.service.service;

import com.appointment.service.dto.AppointmentData;
import com.appointment.service.entity.Appointment;
import com.appointment.service.mapper.AppointmentMapper;
import com.appointment.service.producer.AppointmentCreateProducer;
import com.appointment.service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentCreateProducer appointmentCreateProducer;

    public void appointmentCreate(AppointmentData appointmentData) {
        log.info("Appointment creation process started.");
        Appointment appointment = new Appointment();
        try{
            appointment = appointmentRepository.save(AppointmentMapper.toAppointment(appointmentData.getAppointmentDto()));
            appointmentData.setAppointmentDto(AppointmentMapper.toAppointmentDto(appointment));
            appointmentData.getPaymentDto().setAppointmentId(appointment.getId());
            appointmentCreateProducer.appointmentCreate(appointmentData);
            log.info("Appointment creation process completed successfully.");
        }catch (Exception exception){
            log.error("Exception occurred during appointment creation: {}", exception.getMessage(), exception);
            if(Objects.nonNull(appointment.getId())){
                appointmentRepository.deleteById(appointment.getId());
            }
        }
    }


    @RabbitListener(
            bindings =
            @QueueBinding(
                    value = @Queue(value = "appointment.canceled.request"),
                    exchange = @Exchange(value = "appointment.exchange"),
                    key = "appointment.canceled"
            )
    )
    public void  appointmentCanceled(AppointmentData appointmentData){
        log.info("Processing appointment canceled request");
        if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getAppointmentDto().getId())){
            appointmentRepository.deleteById(appointmentData.getAppointmentDto().getId());
        }
    }

}
