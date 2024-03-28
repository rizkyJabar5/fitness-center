package com.jabar.app.fitnesscenter.feature.fitness.entity.repositories;

import com.jabar.app.fitnesscenter.feature.fitness.entity.model.HealthProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HealthProgramRepository extends JpaRepository<HealthProgram, String> {

    @Query("select h from HealthProgram h")
    Page<HealthProgram> findBy(Pageable pageable);
}