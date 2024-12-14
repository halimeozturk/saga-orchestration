package com.payment.service.mapper;

import com.payment.service.dto.AppointmentData;
import com.payment.service.dto.PaymentDto;
import com.payment.service.entity.Payment;

public class PaymentMapper {

    public static Payment toAppointment(AppointmentData appointmentData) {
        if (appointmentData == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(appointmentData.getPaymentDto().getId());
        payment.setPatientId(appointmentData.getPaymentDto().getPatientId());
        payment.setCurrency(appointmentData.getPaymentDto().getCurrency());
        payment.setTotalAmount(appointmentData.getPaymentDto().getTotalAmount());
        payment.setAppointmentId(appointmentData.getAppointmentDto().getId());

        return payment;
    }

    public static PaymentDto toAppointmentDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setAppointmentId(payment.getAppointmentId());
        paymentDto.setCurrency(payment.getCurrency());
        paymentDto.setPatientId(payment.getPatientId());
        paymentDto.setTotalAmount(payment.getTotalAmount());

        return paymentDto;
    }
}
