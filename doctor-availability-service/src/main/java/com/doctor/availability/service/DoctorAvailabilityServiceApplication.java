package com.doctor.availability.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DoctorAvailabilityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctorAvailabilityServiceApplication.class, args);
    }

}
