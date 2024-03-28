package com.jabar.app.fitnesscenter.feature.fitness.entity.dto;

import com.jabar.app.fitnesscenter.feature.fitness.entity.model.ProgramDetail;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link ProgramDetail}
 */
@Builder
public record ProgramDetailDto(
        String id,
        String exercise,
        String description,
        LocalTime duration) implements Serializable {
}