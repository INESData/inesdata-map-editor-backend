package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.Namespace;

/**
 * Repository interface for managing Namespace entities.
 *
 */
@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {

}
