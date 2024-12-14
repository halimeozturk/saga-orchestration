package com.doctor.availability.service.producer;

import com.doctor.availability.service.dto.AppointmentData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConfirmedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void appointmentCreate(AppointmentData appointmentData) throws JsonProcessingException {
        String reservationConfirmed = new ObjectMapper().writeValueAsString(appointmentData);
        log.info("[x] Requesting reservationConfirmed ({})", reservationConfirmed);
        rabbitTemplate.convertAndSend("reservation.exchange",
                "reservation.confirmed", appointmentData);
    }
}
