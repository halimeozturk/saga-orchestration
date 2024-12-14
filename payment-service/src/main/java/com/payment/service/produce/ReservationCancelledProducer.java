package com.payment.service.produce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.service.dto.AppointmentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCancelledProducer {

    private final RabbitTemplate rabbitTemplate;

    public void reservationCancelled(AppointmentData appointmentData) throws JsonProcessingException {
        String reservationCancelled = new ObjectMapper().writeValueAsString(appointmentData);
        log.info("[x] Requesting reservationCancelled ({})", reservationCancelled);
        rabbitTemplate.convertAndSend("reservation.exchange",
                "reservation.cancelled", appointmentData);
    }
}