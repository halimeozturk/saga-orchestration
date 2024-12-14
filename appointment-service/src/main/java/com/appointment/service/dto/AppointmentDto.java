package com.appointment.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDto {

    private Long id;
    private Long patientId;           // Hasta kimliği
    private Long doctorId;            // Doktor kimliği
    private String appointmentDate;   // Randevu tarihi (yyyy-MM-dd formatında)
    private String appointmentTime;   // Randevu saati (HH:mm formatında)


}
