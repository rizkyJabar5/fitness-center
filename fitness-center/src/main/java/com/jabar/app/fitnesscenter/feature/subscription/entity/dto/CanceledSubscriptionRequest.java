package com.jabar.app.fitnesscenter.feature.subscription.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record CanceledSubscriptionRequest(
        @NotBlank
        @NotNull
        String healthProgramId,

        @NotBlank
        @NotNull
        String subscriptionId) implements Serializable {
}
