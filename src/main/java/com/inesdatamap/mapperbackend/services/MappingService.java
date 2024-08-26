package com.inesdatamap.mapperbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;

/**
 * Service interface for managing mappings.
 */
public interface MappingService {

	/**
	 * Retrieves all mappings.
	 *
	 * @param pageable
	 *            pageable
	 *
	 * @return List of mappings
	 */
	Page<MappingDTO> listMappings(Pageable pageable);

}
