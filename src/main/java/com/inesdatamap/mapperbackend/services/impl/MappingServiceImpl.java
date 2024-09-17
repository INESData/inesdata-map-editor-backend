package com.inesdatamap.mapperbackend.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.MappingMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.GraphEngineService;
import com.inesdatamap.mapperbackend.services.MappingService;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the MappingService interface.
 */
@Service
public class MappingServiceImpl implements MappingService {

	@Autowired
	private MappingRepository mappingRepo;

	@Autowired
	private GraphEngineService graphEngineService;

	@Autowired
	private MappingMapper mappingMapper;

	@Autowired
	private OntologyRepository ontologyRepository;

	@Autowired
	private DataSourceRepository<DataSource> dataSourceRepository;

	/**
	 * Retrieves a list of all mappings and maps them to their corresponding DTOs.
	 *
	 * @param pageable
	 * 	pageable
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
			searchMapping.setId(mapping.getId());
			searchMapping.setName(mapping.getName());

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
	 * Deletes a mapping by its id.
	 *
	 * @param id
	 * 	the ID of the mapping to delete
	 */
	@Override
	public void deleteMapping(Long id) {

		// Get entity if exists
		this.getEntity(id);

		this.mappingRepo.deleteById(id);

	}

	/**
	 * Retrieves a MappingField entity by its ID.
	 *
	 * @param id
	 * 	the ID of the MappingField to retrieve
	 *
	 * @return the MappingField entity corresponding to the given ID
	 */
	@Override
	public Mapping getEntity(Long id) {
		return this.mappingRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
	}

	/**
	 * Creates a new mapping.
	 *
	 * @param mappingDTO
	 * 	the mapping to create
	 *
	 * @return the created mapping
	 */
	@Override
	public MappingDTO create(MappingDTO mappingDTO) {

		Mapping mapping = setRelationships(mappingDTO);

		byte[] rml = buildRml(mappingDTO);
		mapping.setRml(rml);

		return this.mappingMapper.entityToDto(this.mappingRepo.save(mapping));
	}

	/**
	 * Sets the relationships of a mapping.
	 *
	 * @param mappingDTO
	 * 	the mapping dto
	 *
	 * @return the mapping with the relationships set
	 */
	private Mapping setRelationships(MappingDTO mappingDTO) {

		Mapping mapping = mappingMapper.dtoToEntity(mappingDTO);

		if (!CollectionUtils.isEmpty(mapping.getFields())) {

			mapping.getFields().forEach(field -> {

				Ontology ontology = this.ontologyRepository.getReferenceById(field.getOntology().getId());
				DataSource dataSource = this.dataSourceRepository.getReferenceById(field.getSource().getId());

				field.setOntology(ontology);
				field.setSource(dataSource);

			});
		}

		return mapping;
	}

	private static byte[] buildRml(MappingDTO mapping) {

		mapping.getFields().forEach(field -> {

			// TODO

		});

		return new byte[0];

	}

	/**
	 * Materializes a mapping by its id.
	 *
	 * @param id
	 * 	the ID of the mapping to materialize
	 *
	 * @return the results of the materialization
	 */
	@Override
	public List<String> materialize(Long id) {

		Mapping mapping = this.getEntity(id);
		List<String> results;
		File rmlTmpFile = null;

		try {

			// Create temporary file
			rmlTmpFile = FileUtils.createTemporaryFile(mapping.getRml());

			// Run the graph engine
			results = this.graphEngineService.run(rmlTmpFile.getAbsolutePath(), mapping.getId(), mapping.getFields());

		} catch (GraphEngineException e) {
			// Delete temporary file
			if (rmlTmpFile != null) {
				FileUtils.deleteFile(rmlTmpFile.toPath());
			}
			throw e;
		}

		// Delete temporary file
		FileUtils.deleteFile(rmlTmpFile.toPath());

		return results;
	}

}
