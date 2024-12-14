package com.appointment.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentData {
    @NotNull(message = "AppointmentDto can not be null")
    private AppointmentDto appointmentDto;

    @NotNull(message = "PaymentDto can not be null")
    private PaymentDto paymentDto;

    private DoctorAvailabilityDto doctorAvailabilityDto;
    private NotificationDto notificationDto;
}
