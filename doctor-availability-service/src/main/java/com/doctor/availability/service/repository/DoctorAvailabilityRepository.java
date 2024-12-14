package com.doctor.availability.service.repository;

import com.doctor.availability.service.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DoctorAvailabilityRepository extends CrudRepository<DoctorAvailability, Long> {

    @Query("""
        SELECT da
        FROM DoctorAvailability da
        WHERE da.doctorId = :doctorId
          AND da.startTime <= :requestedTime
          AND da.endTime > :requestedTime
          AND da.isBooked = false
    """)
    DoctorAvailability findAvailableSlot(@Param("doctorId") Long doctorId, @Param("requestedTime") LocalDateTime requestedTime);
}
