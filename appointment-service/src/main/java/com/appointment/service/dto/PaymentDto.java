package com.appointment.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {

    private Long id;
    private Long appointmentId;   // İlgili randevu kimliği
    private Long patientId;       // Hasta kimliği
    private double totalAmount;
    private String currency;

}
