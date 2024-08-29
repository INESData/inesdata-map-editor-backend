package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;

import jakarta.persistence.EntityNotFoundException;

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

		SearchOntologyDTO searchOntologyDTO1 = new SearchOntologyDTO();
		SearchOntologyDTO searchOntologyDTO2 = new SearchOntologyDTO();
		List<SearchOntologyDTO> searchOntologyDTOs = Arrays.asList(searchOntologyDTO1, searchOntologyDTO2);
		Page<SearchOntologyDTO> searchOntologyDTOPage = new PageImpl<>(searchOntologyDTOs);

		// Mock behavior
		Mockito.when(this.ontologyRepo.findAll(Mockito.any(Pageable.class))).thenReturn(ontologyPage);
		Mockito.when(this.ontologyMapper.entitytoSearchOntologyDTO(ontology1)).thenReturn(searchOntologyDTO1);
		Mockito.when(this.ontologyMapper.entitytoSearchOntologyDTO(ontology2)).thenReturn(searchOntologyDTO2);

		// Test
		Pageable pageable = PageRequest.of(0, 10);
		Page<SearchOntologyDTO> result = this.ontologyService.listOntologies(pageable);

		// Verify
		assertEquals(searchOntologyDTOPage.getTotalElements(), result.getTotalElements());
		assertEquals(searchOntologyDTOPage.getContent().size(), result.getContent().size());
	}

	@Test
	void testDeleteOntology() {
		// Mock data
		Long id = 1L;
		Ontology ontology = new Ontology();

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontology));

		// Test
		this.ontologyService.deleteOntology(id);

		// Verify
		Mockito.verify(this.ontologyRepo, Mockito.times(1)).deleteById(id);
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
	void testCreateOntology_withFile() throws IOException {
		// Arrange
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Test Ontology");

		MockMultipartFile file = new MockMultipartFile("file", "testfile.csv", "text/csv", "File content".getBytes());

		Ontology ontology = new Ontology();
		Ontology savedOntology = new Ontology();
		savedOntology.setId(1L);

		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(ontology);
		when(this.ontologyRepo.save(ontology)).thenReturn(savedOntology);
		when(this.ontologyMapper.entityToDto(savedOntology)).thenReturn(dto);

		// Act
		OntologyDTO result = this.ontologyService.createOntology(dto, file);

		// Assert
		assertEquals(dto, result);
		verify(this.ontologyRepo).save(ontology);
		assertEquals("File content".getBytes().length, ontology.getContent().length);
	}

	@Test
	void testCreateOntology_exceptions() throws IOException {
		// OntologyDto is null, should throw IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.createOntology(null, mock(MultipartFile.class));
		}, "The ontology has no data");

		// OntologyDto is valid, file is null (no exception is thrown)
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Test Ontology");
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(new Ontology());
		when(this.ontologyRepo.save(any(Ontology.class))).thenReturn(new Ontology());

		assertDoesNotThrow(() -> {
			this.ontologyService.createOntology(dto, null);
		});

		// OntologyDto is valid, file is empty (no exception is thrown)
		MultipartFile emptyFile = mock(MultipartFile.class);
		when(emptyFile.isEmpty()).thenReturn(true);

		assertDoesNotThrow(() -> {
			this.ontologyService.createOntology(dto, emptyFile);
		});

		// OntologyDto is valid, file has content (no exception is thrown)
		MultipartFile validFile = mock(MultipartFile.class);
		when(validFile.isEmpty()).thenReturn(false);
		when(validFile.getBytes()).thenReturn("valid content".getBytes());

		assertDoesNotThrow(() -> {
			this.ontologyService.createOntology(dto, validFile);
		});

	}

	@Test
	void testGetEntityThrowsException() {
		// Mock data
		Long id = 1L;

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.ontologyService.getEntity(id));
	}
}
