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
public class AppointmentCancelledProducer {

    private final RabbitTemplate rabbitTemplate;

    public void appointmentCancelled(AppointmentData appointmentData) throws JsonProcessingException {
        String appointmentCancelled = new ObjectMapper().writeValueAsString(appointmentData);
        log.info("[x] Requesting appointmentCancelled ({})", appointmentCancelled);
        rabbitTemplate.convertAndSend("appointment.exchange",
                "appointment.canceled", appointmentData);
    }
}
