package com.inesdatamap.mapperbackend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.services.MappingService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the MappingService interface.
 *
 */
@Service
public class MappingServiceImpl implements MappingService {

	@Autowired
	private MappingRepository mappingRepo;

	/**
	 * Retrieves a list of all mappings and maps them to their corresponding DTOs.
	 *
	 * @param pageable
	 *            pageable
	 *
	 * @return List of MappingsDTOs
	 */
	@Override
	public Page<SearchMappingDTO> listMappings(Pageable pageable) {

		// Get all mappings
		Page<Mapping> mappingsPage = this.mappingRepo.findAll(pageable);

		List<SearchMappingDTO> searchMappingList = new ArrayList<>();

		// Iterate mappings page
		for (Mapping mapping : mappingsPage) {
			SearchMappingDTO searchMapping = new SearchMappingDTO();
			if (mapping.getId() != null) {
				searchMapping.setId(mapping.getId());
			}
			if (mapping.getName() != null && !mapping.getName().isEmpty()) {
				searchMapping.setName(mapping.getName());
			}

			List<String> ontologies = new ArrayList<>();
			List<String> dataSources = new ArrayList<>();

			// Iterate fields list and set values to ontologies and sources list
			for (MappingField field : mapping.getFields()) {
				if (field.getOntology() != null && field.getOntology().getName() != null) {
					ontologies.add(field.getOntology().getName());
				}
				if (field.getSource() != null && field.getSource().getName() != null) {
					dataSources.add(field.getSource().getName());
				}
			}
			// Set ontologies and sources lists to DTO
			searchMapping.setOntologies(ontologies);
			searchMapping.setDataSources(dataSources);

			// Set DTO to list
			searchMappingList.add(searchMapping);
		}

		// Convert from list to Page
		return new PageImpl<>(searchMappingList, pageable, mappingsPage.getTotalElements());

	}

	/**
	 * Deletes an mapping by its id.
	 *
	 * @param id
	 *            the ID of the mapping to delete
	 */
	@Override
	public void deleteMapping(Long id) {

		// Get entity if exists
		Mapping mapping = this.getEntity(id);

		this.mappingRepo.deleteById(id);

	}

	/**
	 * Retrieves a MappingField entity by its ID.
	 *
	 * @param id
	 *            the ID of the MappingField to retrieve
	 * @return the MappingField entity corresponding to the given ID
	 */
	@Override
	public Mapping getEntity(Long id) {
		return this.mappingRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

}
