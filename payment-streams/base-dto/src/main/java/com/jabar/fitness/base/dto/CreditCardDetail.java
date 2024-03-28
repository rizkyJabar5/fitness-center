package com.jabar.fitness.base.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record CreditCardDetail(
        String placeholder,
        String cardNumber) implements Serializable {
}