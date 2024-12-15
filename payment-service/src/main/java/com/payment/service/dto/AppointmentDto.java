package com.payment.service.dto;

import com.payment.service.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDto {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private AppointmentStatus status;
}
