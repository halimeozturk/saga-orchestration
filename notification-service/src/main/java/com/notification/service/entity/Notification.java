package com.notification.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // Benzersiz bildirim kimliği

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;       // İlgili randevu kimliği

    @Column(name = "patient_id", nullable = false)
    private Long patientId;           // Hasta kimliği

    @Column(nullable = false)
    private String message;           // Gönderilen mesaj

    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "updated_date",nullable = false)
    private LocalDateTime updatedDate;

}
