package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;

/**
 * Unit tests for the {@link OntologyServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(MockitoExtension.class)
class OntologyServiceImplTest {

	@Mock
	private OntologyRepository ontologyRepo;

	@Mock
	private OntologyMapper ontologyMapper;

	@InjectMocks
	private OntologyServiceImpl ontologyService;

	@Test
	void testListOntologies() {
		// Mock data
		Ontology ontology1 = new Ontology();
		Ontology ontology2 = new Ontology();
		List<Ontology> ontologies = Arrays.asList(ontology1, ontology2);
		Page<Ontology> ontologyPage = new PageImpl<>(ontologies);

		OntologyDTO ontologyDTO1 = new OntologyDTO();
		OntologyDTO ontologyDTO2 = new OntologyDTO();
		List<OntologyDTO> ontologyDTOs = Arrays.asList(ontologyDTO1, ontologyDTO2);
		Page<OntologyDTO> ontologyDTOPage = new PageImpl<>(ontologyDTOs);

		// Mock behavior
		Mockito.when(this.ontologyRepo.findAll(Mockito.any(Pageable.class))).thenReturn(ontologyPage);
		Mockito.when(this.ontologyMapper.entityToDto(ontology1)).thenReturn(ontologyDTO1);
		Mockito.when(this.ontologyMapper.entityToDto(ontology2)).thenReturn(ontologyDTO2);

		// Test
		Pageable pageable = PageRequest.of(0, 10);
		Page<OntologyDTO> result = this.ontologyService.listOntologies(pageable);

		// Verify
		assertEquals(ontologyDTOPage.getTotalElements(), result.getTotalElements());
		assertEquals(ontologyDTOPage.getContent().size(), result.getContent().size());
	}

	@Test
	void testUpdateOntology() {
		// Mock data
		Long id = 1L;
		Ontology ontologyDB = new Ontology();
		OntologyDTO ontologyDTO = new OntologyDTO();
		Ontology ontologySource = new Ontology();
		Ontology ontologyUpdated = new Ontology();

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontologyDB));
		Mockito.when(this.ontologyMapper.dtoToEntity(ontologyDTO)).thenReturn(ontologySource);
		Mockito.when(this.ontologyMapper.merge(ontologySource, ontologyDB)).thenReturn(ontologyUpdated);
		Mockito.when(this.ontologyRepo.saveAndFlush(ontologyUpdated)).thenReturn(ontologyUpdated);
		Mockito.when(this.ontologyMapper.entityToDto(ontologyUpdated)).thenReturn(ontologyDTO);

		// Test
		OntologyDTO result = this.ontologyService.updateOntology(id, ontologyDTO);

		// Verify
		assertEquals(ontologyDTO, result);
	}

	@Test
	void testGetEntity() {
		// Mock data
		Long id = 1L;
		Ontology ontology = new Ontology();

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontology));

		// Test
		Ontology result = this.ontologyService.getEntity(id);

		// Verify
		assertEquals(ontology, result);
	}

	@Test
	void testGetEntityThrowsException() {
		// Mock data
		Long id = 1L;

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(NoSuchElementException.class, () -> this.ontologyService.getEntity(id));
	}
}
