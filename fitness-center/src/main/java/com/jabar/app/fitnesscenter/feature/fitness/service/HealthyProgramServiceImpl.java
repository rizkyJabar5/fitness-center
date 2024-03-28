package com.jabar.app.fitnesscenter.feature.fitness.service;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.app.common.exceptions.ResourceNotFoundException;
import com.jabar.app.fitnesscenter.app.util.DateConverter;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.HealthProgramDto;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.HealthProgramRequest;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.ProgramDetailRequest;
import com.jabar.app.fitnesscenter.feature.fitness.entity.mapper.HealthProgramMapper;
import com.jabar.app.fitnesscenter.feature.fitness.entity.model.HealthProgram;
import com.jabar.app.fitnesscenter.feature.fitness.entity.model.ProgramDetail;
import com.jabar.app.fitnesscenter.feature.fitness.entity.repositories.HealthProgramRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.NOT_FOUND_MSG;
import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.messageFormat;
import static com.jabar.app.fitnesscenter.app.util.DateConverter.DATETIME_FORMAT;

@Service
@RequiredArgsConstructor
public class HealthyProgramServiceImpl implements HealthyProgramService {
    private final HealthProgramRepository healthProgramRepository;
    private final ProgramDetailService programDetailService;
    private final HealthProgramMapper healthProgramMapper;

    @Override
    public BaseResponse<List<HealthProgramDto>> getAllHealthProgram() {
        List<HealthProgramDto> programDtoPage = healthProgramRepository
                .findAll()
                .stream()
                .map(healthProgramMapper::toDto)
                .toList();
        return new BaseResponse<>(programDtoPage);
    }

    @Transactional
    @Override
    public BaseResponse<HealthProgramDto> createNewProgramHealth(HealthProgramRequest request) {
        Date schedule = DateConverter.parseToDate(request.schedule(), DATETIME_FORMAT);

        HealthProgram healthProgram = new HealthProgram();
        healthProgram.setId(UUID.randomUUID().toString());
        healthProgram.setCreatedAt(new Date());
        healthProgram.setProgramName(request.nameOfProgram());
        healthProgram.setPrice(request.price());
        healthProgram.setSchedule(schedule);
        healthProgram.setTotalMeetings(request.totalMeeting());
        healthProgram = healthProgramRepository.saveAndFlush(healthProgram);

        for (ProgramDetailRequest program : request.programDetails()) {
            ProgramDetail programDetail = programDetailService.createOrUpdate(program);
            healthProgram.addProgramDetails(programDetail);
        }

        healthProgramRepository.save(healthProgram);

        HealthProgramDto mapperDto = healthProgramMapper.toDto(healthProgram);
        return new BaseResponse<>(mapperDto);
    }

    @Override
    public HealthProgram getHealthyProgramById(String id) {
        return this.getHealthProgramById(id);
    }

    @Override
    public void updatePersistentProgramHealth(HealthProgram entity) {
        healthProgramRepository.save(entity);
    }

    private HealthProgram getHealthProgramById(String id) {
        String message = messageFormat(NOT_FOUND_MSG, id);
        return healthProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(message));
    }
}
