package com.appointment.service.controller;

import com.appointment.service.dto.AppointmentData;
import com.appointment.service.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public void appointmentCreate(@RequestBody AppointmentData appointmentData){
        appointmentService.appointmentCreate(appointmentData);
    }
}
