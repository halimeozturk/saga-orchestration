package com.notification.service.mapper;

import com.notification.service.dto.AppointmentData;
import com.notification.service.entity.Notification;

import java.util.Random;

public class NotificationMapper {

    public static Notification toAppointment(AppointmentData appointmentData) {
        if (appointmentData == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setAppointmentId(appointmentData.getAppointmentDto().getId());
        notification.setPatientId(appointmentData.getAppointmentDto().getPatientId());
        notification.setMessage("Your appointment has been created successfully.");
        return notification;
    }

}
