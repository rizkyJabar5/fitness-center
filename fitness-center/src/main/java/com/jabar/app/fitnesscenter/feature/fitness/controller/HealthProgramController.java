package com.jabar.app.fitnesscenter.feature.fitness.controller;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.HealthProgramDto;
import com.jabar.app.fitnesscenter.feature.fitness.entity.dto.HealthProgramRequest;
import com.jabar.app.fitnesscenter.feature.fitness.service.HealthyProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.HEALTH_PROGRAMS_URL;

@RestController
@RequestMapping(HEALTH_PROGRAMS_URL)
@RequiredArgsConstructor
public class HealthProgramController {
    private final HealthyProgramService programService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<HealthProgramDto>>> getAllHealthProgram() {
        var allHealthProgram = programService.getAllHealthProgram();
        return new ResponseEntity<>(allHealthProgram, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<HealthProgramDto>> createNewProgramHealth(@Valid @RequestBody HealthProgramRequest request) {
        BaseResponse<HealthProgramDto> baseResponse = programService.createNewProgramHealth(request);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
