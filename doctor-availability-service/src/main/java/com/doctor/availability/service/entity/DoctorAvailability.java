package com.doctor.availability.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_availability")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // Benzersiz kimlik

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;      // Başlangıç zamanı (tarih + saat)

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;        // Bitiş zamanı (tarih + saat)

    @Column(name = "is_booked", nullable = false)
    private Boolean isBooked;             // Zaman dilimi rezerve edilmiş mi?

    @Column(name = "updated_date",nullable = false)
    private LocalDateTime updatedDate;
}
