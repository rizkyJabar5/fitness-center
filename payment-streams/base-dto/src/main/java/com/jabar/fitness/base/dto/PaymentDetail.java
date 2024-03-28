package com.jabar.fitness.base.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record PaymentDetail(
        Double totalPay,
        String cardNumber) implements Serializable {
}