package com.jabar.app.fitnesscenter.feature.fitness.service;

import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.ProgramDetailDto;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.ProgramDetailRequest;
import com.jabar.app.fitnesscenter.feature.fitness.entity.model.ProgramDetail;

import java.util.List;

public interface ProgramDetailService {
    ProgramDetailDto findProgramById(String id);

    List<ProgramDetailDto> findAll();

    ProgramDetail createOrUpdate(ProgramDetailRequest request);

}
