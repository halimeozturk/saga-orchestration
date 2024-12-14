package com.appointment.service.mapper;

import com.appointment.service.dto.AppointmentDto;
import com.appointment.service.entity.Appointment;

public class AppointmentMapper {

    public static Appointment toAppointment(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        appointment.setPatientId(appointmentDto.getPatientId());
        appointment.setDoctorId(appointmentDto.getDoctorId());

        return appointment;
    }

    public static AppointmentDto toAppointmentDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setPatientId(appointment.getPatientId());
        appointmentDto.setDoctorId(appointment.getDoctorId());

        return appointmentDto;
    }
}