package com.notification.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.service.dto.AppointmentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRollbackProducer {

    private final RabbitTemplate rabbitTemplate;

    public void paymentRollback(AppointmentData appointmentData) throws JsonProcessingException {
        String paymentRollback = new ObjectMapper().writeValueAsString(appointmentData);
        log.info("[x] Requesting paymentRollback ({})", paymentRollback);
        rabbitTemplate.convertAndSend("payment.exchange", "payment.rollback", appointmentData);
    }
}
