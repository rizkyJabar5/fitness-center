package com.jabar.fitness.base.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record KafkaPaymentResponse(String timestamp, String status) implements Serializable {

}
