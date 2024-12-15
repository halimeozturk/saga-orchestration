package com.appointment.service.producer;

import com.appointment.service.dto.AppointmentData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentCreateProducer {

    private final RabbitTemplate rabbitTemplate;

    public void appointmentCreate(AppointmentData appointmentData) throws JsonProcessingException {
        String appointmentCreate = new ObjectMapper().writeValueAsString(appointmentData);
        log.info("[x] Requesting appointmentCreate ({})", appointmentCreate);
        rabbitTemplate.convertAndSend("appointment.exchange",
                "appointment.created", appointmentData);
    }
}
