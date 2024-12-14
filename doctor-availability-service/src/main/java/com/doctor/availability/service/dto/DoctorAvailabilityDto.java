package com.doctor.availability.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorAvailabilityDto {
    private Long id;
    private boolean isBooked = false;


}
