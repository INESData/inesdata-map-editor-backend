package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * Repository interface for managing FileSource entities.
 *
 */
@Repository
public interface FileSourceRepository extends JpaRepository<FileSource, Long> {

}
