package com.jabar.app.fitnesscenter.feature.fitness.service;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.HealthProgramDto;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.HealthProgramRequest;
import com.jabar.app.fitnesscenter.feature.fitness.entity.model.HealthProgram;

import java.util.List;

public interface HealthyProgramService {
    BaseResponse<List<HealthProgramDto>> getAllHealthProgram();

    BaseResponse<HealthProgramDto> createNewProgramHealth(HealthProgramRequest request);

    HealthProgram getHealthyProgramById(String id);

    void updatePersistentProgramHealth(HealthProgram entity);
}
