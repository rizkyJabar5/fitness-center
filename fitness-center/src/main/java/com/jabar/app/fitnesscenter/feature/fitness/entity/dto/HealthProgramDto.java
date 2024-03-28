package com.jabar.app.fitnesscenter.feature.fitness.entity.dto;

import com.jabar.app.fitnesscenter.feature.fitness.entity.model.HealthProgram;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link HealthProgram}
 */
@Builder
public record HealthProgramDto(
        String id,
        Date createdAt,
        Date lastModifiedDate,
        String programName,
        Double price,
        Date schedule,
        Integer totalMeeting,
        Set<ProgramDetailDto> programDetails) implements Serializable {
}