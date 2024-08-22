package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
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
	void testUpdateOntology_withFile() throws IOException {
		// Arrange
		Long id = 1L;
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Updated Ontology");

		// Simulate file upload
		MockMultipartFile file = new MockMultipartFile("file", "testfile.csv", "text/csv", "File content".getBytes());

		Ontology ontologyDB = new Ontology();
		Ontology ontologySource = new Ontology();
		Ontology ontologyUpdated = new Ontology();

		when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontologyDB));
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(ontologySource);
		when(this.ontologyMapper.merge(ontologySource, ontologyDB)).thenReturn(ontologyUpdated);
		when(this.ontologyRepo.saveAndFlush(ontologyUpdated)).thenReturn(ontologyUpdated);
		when(this.ontologyMapper.entityToDto(ontologyUpdated)).thenReturn(dto);

		// Act
		OntologyDTO result = this.ontologyService.updateOntology(id, dto, file);

		// Assert
		assertEquals(dto, result);
		verify(this.ontologyRepo).saveAndFlush(ontologyUpdated);
		assertArrayEquals("File content".getBytes(), ontologyDB.getContent());
	}

	@Test
	void testUpdateOntology_exceptions() throws IOException {
		Long id = 1L;

		// Mock the getEntity method to return a valid ontology
		Ontology ontology = new Ontology();
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontology));

		// Caso 1: ontologyDto es válido, file es null (no se lanza excepción)
		OntologyDTO dto = new OntologyDTO();
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(new Ontology());

		assertDoesNotThrow(() -> {
			this.ontologyService.updateOntology(id, dto, null);
		});

		// Caso 2: ontologyDto es válido, file es vacío (no se lanza excepción)
		MultipartFile emptyFile = mock(MultipartFile.class);
		when(emptyFile.isEmpty()).thenReturn(true);

		assertDoesNotThrow(() -> {
			this.ontologyService.updateOntology(id, dto, emptyFile);
		});

		// Caso 3: ontologyDto es válido, file tiene contenido y se procesa correctamente
		MultipartFile validFile = mock(MultipartFile.class);
		when(validFile.isEmpty()).thenReturn(false);
		when(validFile.getBytes()).thenReturn("valid content".getBytes()); // Aquí se lanza IOException
		when(validFile.getContentType()).thenReturn("application/json");

		assertDoesNotThrow(() -> {
			this.ontologyService.updateOntology(id, dto, validFile);
		});

		// Caso 4: ontologyDto es válido, file tiene error de lectura (debería lanzar UncheckedIOException)
		MultipartFile fileWithError = mock(MultipartFile.class);
		when(fileWithError.isEmpty()).thenReturn(false);
		when(fileWithError.getContentType()).thenReturn("application/json");

		try {
			when(fileWithError.getBytes()).thenThrow(new IOException("Failed to read file"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		UncheckedIOException exception = assertThrows(UncheckedIOException.class, () -> {
			this.ontologyService.updateOntology(id, dto, fileWithError);
		});
		assertEquals("Failed to store file content", exception.getMessage());
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
	void testCreateOntology_exceptions() {
		// Arrange
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Test Ontology");

		// Test with null DTO
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.createOntology(null, mock(MultipartFile.class));
		}, "The ontology has no data");

		// Test with null file
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.createOntology(dto, null);
		}, "File is required and cannot be empty");

		// Test with empty file
		MultipartFile emptyFile = mock(MultipartFile.class);
		when(emptyFile.isEmpty()).thenReturn(true);
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.createOntology(dto, emptyFile);
		}, "File is required and cannot be empty");
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
