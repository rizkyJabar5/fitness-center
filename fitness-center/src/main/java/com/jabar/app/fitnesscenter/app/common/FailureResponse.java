package com.jabar.app.fitnesscenter.app.common;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record FailureResponse(
        String timestamp,
        String message,
        String code,
        String path) implements Serializable {
}
