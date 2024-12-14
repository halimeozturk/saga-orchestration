package com.payment.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payment.service.dto.AppointmentData;
import com.payment.service.entity.Payment;
import com.payment.service.mapper.PaymentMapper;
import com.payment.service.produce.PaymentProducer;
import com.payment.service.produce.ReservationCancelledProducer;
import com.payment.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;
    private final ReservationCancelledProducer reservationCancelledProducer;

    @RabbitListener(
            bindings =
            @QueueBinding(
                    value = @Queue(value = "reservation.confirmed.request"),
                    exchange = @Exchange(value = "reservation.exchange"),
                    key = "reservation.confirmed"
            )
    )
    public void reservationConfirmed(AppointmentData appointmentData) throws JsonProcessingException {
        try{
            if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getAppointmentDto().getId())) {
                Payment payment = paymentRepository.save(PaymentMapper.toAppointment(appointmentData));
                appointmentData.getPaymentDto().setId(payment.getId());
                paymentProducer.payment(appointmentData);
                log.info("Payment created for appointmentId: {} with paymentId: {}", appointmentData.getAppointmentDto().getId(), payment.getId());
            }else{
                log.warn("Appointment data is missing or invalid, cancelling reservation for appointment ");
                reservationCancelledProducer.reservationCancelled(appointmentData);
            }
        }catch (Exception e){
            if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getPaymentDto().getId())) {
                paymentRepository.deleteById(appointmentData.getPaymentDto().getId());
                log.error("Payment rollback for appointmentId: {}", appointmentData.getAppointmentDto().getId());
            }
            reservationCancelledProducer.reservationCancelled(appointmentData);
            log.error("Reservation cancelled due to error for appointmentId: {}",
                    appointmentData != null && appointmentData.getAppointmentDto() != null ? appointmentData.getAppointmentDto().getId() : "Unknown");
        }
    }


    @RabbitListener(
            bindings =
            @QueueBinding(
                    value = @Queue(value = "payment.rollback.request"),
                    exchange = @Exchange(value = "payment.exchange"),
                    key = "payment.rollback"
            )
    )
    public void paymentRollback(AppointmentData appointmentData) throws JsonProcessingException {
        if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getPaymentDto().getId())) {
            paymentRepository.deleteById(appointmentData.getPaymentDto().getId());
            log.error("Payment rollback for appointmentId: {}", appointmentData.getAppointmentDto().getId());
        }
        reservationCancelledProducer.reservationCancelled(appointmentData);
        log.error("Reservation cancelled due to error for appointmentId: {}",
                appointmentData != null && appointmentData.getAppointmentDto() != null ? appointmentData.getAppointmentDto().getId() : "Unknown");

    }
}
