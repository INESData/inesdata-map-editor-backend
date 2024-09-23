package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.Execution;

/**
 * Repository interface for managing {@link com.inesdatamap.mapperbackend.model.jpa.Execution} entities.
 */
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

}
