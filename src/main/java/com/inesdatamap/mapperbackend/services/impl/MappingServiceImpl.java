package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.mappers.MappingMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.services.MappingService;

/**
 * Implementation of the MappingService interface.
 *
 */
@Service
public class MappingServiceImpl implements MappingService {

	@Autowired
	private MappingRepository mappingRepo;

	@Autowired
	private MappingMapper mappingMapper;

	/**
	 * Retrieves a list of all mappings and maps them to their corresponding DTOs.
	 *
	 * @param pageable
	 *            pageable
	 *
	 * @return List of MappingsDTOs
	 */
	public Page<MappingDTO> listMappings(Pageable pageable) {

		Page<Mapping> mappingsList = this.mappingRepo.findAll(pageable);

		return mappingsList.map(this.mappingMapper::entityToDto);

	}

}
