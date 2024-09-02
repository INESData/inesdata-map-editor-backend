package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.Mapping;

/**
 * Repository interface for managing Mapping entities.
 *
 */
@Repository
public interface MappingRepository extends JpaRepository<Mapping, Long> {

}
