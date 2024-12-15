package com.doctor.availability.service.dto;

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
}

