package com.notification.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notification.service.dto.AppointmentData;
import com.notification.service.dto.NotificationDto;
import com.notification.service.entity.Notification;
import com.notification.service.mapper.NotificationMapper;
import com.notification.service.producer.PaymentRollbackProducer;
import com.notification.service.repository.NotificationRepository;
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
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PaymentRollbackProducer paymentRollbackProducer;

    @RabbitListener(
            bindings =
            @QueueBinding(
                    value = @Queue(value = "payment.success.request"),
                    exchange = @Exchange(value = "payment.exchange"),
                    key = "payment.success"
            )
    )
    public void paymentSuccess(AppointmentData appointmentData) throws JsonProcessingException {
        try{
            if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getAppointmentDto().getId()) && Objects.nonNull(appointmentData.getPaymentDto().getId())) {
                Notification notification = notificationRepository.save(NotificationMapper.toAppointment(appointmentData));
                appointmentData.setNotificationDto(new NotificationDto(notification.getId()));
                log.info("Notification saved successfully for appointmentId: {} with notificationId: {}",
                        appointmentData.getAppointmentDto().getId(), notification.getId());
            }else{
                if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getNotificationDto().getId())) {
                    notificationRepository.deleteById(appointmentData.getNotificationDto().getId());
                }
                paymentRollbackProducer.paymentRollback(appointmentData);
                log.info("Payment rollback triggered for appointmentId: {}", appointmentData.getAppointmentDto().getId());
            }
        }catch (Exception e){
            if(Objects.nonNull(appointmentData) && Objects.nonNull(appointmentData.getNotificationDto().getId())) {
                notificationRepository.deleteById(appointmentData.getNotificationDto().getId());
            }
            paymentRollbackProducer.paymentRollback(appointmentData);
            log.info("Payment rollback triggered due to error for appointmentId: {}", appointmentData.getAppointmentDto().getId());
        }
    }
}
