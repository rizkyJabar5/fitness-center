package com.jabar.app.fitnesscenter.feature.fitness.entity.repositories;

import com.jabar.app.fitnesscenter.feature.fitness.entity.model.ProgramDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgramDetailRepository extends JpaRepository<ProgramDetail, String> {
    Optional<ProgramDetail> findByExerciseIgnoreCase(String exercise);
}