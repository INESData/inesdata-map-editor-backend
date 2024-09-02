package com.inesdatamap.mapperbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;

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
	Page<SearchMappingDTO> listMappings(Pageable pageable);

	/**
	 * Deletes a mapping by its id.
	 *
	 * @param id
	 *            the ID of the mapping to delete
	 */
	void deleteMapping(Long id);

	/**
	 * Gets entity by its id.
	 *
	 * @param id
	 *            the ID of the mapping to get
	 * @return Mapping
	 */
	Mapping getEntity(Long id);

}
