package com.jabar.app.fitnesscenter.feature.subscription.entity.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record MemberSubscribedRequest(
        @NotBlank
        String paymentStatus) implements Serializable {
}
