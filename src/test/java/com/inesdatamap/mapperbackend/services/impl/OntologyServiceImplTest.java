package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
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

		byte[] fileContent = "Updated file content".getBytes();
		MultipartFile file = new MockMultipartFile("file", "testfile.txt", "text/plain", fileContent);

		Ontology existingOntology = new Ontology();
		existingOntology.setContent("Old content".getBytes()); // Existing content

		Ontology updatedOntology = new Ontology();
		updatedOntology.setId(id); // Set id to match the existing ontology
		updatedOntology.setContent(fileContent); // Updated content

		when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(existingOntology));
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(updatedOntology);
		when(this.ontologyMapper.merge(updatedOntology, existingOntology)).thenReturn(updatedOntology);
		when(this.ontologyRepo.saveAndFlush(updatedOntology)).thenReturn(updatedOntology);
		when(this.ontologyMapper.entityToDto(updatedOntology)).thenReturn(dto);

		// Act
		OntologyDTO result = this.ontologyService.updateOntology(id, dto, file);

		// Assert
		assertEquals(dto, result);
		verify(this.ontologyRepo).saveAndFlush(updatedOntology);
		assertArrayEquals(fileContent, existingOntology.getContent()); // Verify that the file content was updated
	}

	void testUpdateOntology_fileReadException() throws IOException {
		// Arrange
		Long id = 1L;
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Updated Ontology");

		MultipartFile file = mock(MultipartFile.class);
		when(file.getBytes()).thenThrow(new IOException("Failed to read file"));

		Ontology existingOntology = new Ontology();
		when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(existingOntology));
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(new Ontology());

		// Act & Assert
		assertThrows(UncheckedIOException.class, () -> {
			this.ontologyService.updateOntology(id, dto, file);
		});
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

		MockMultipartFile file = new MockMultipartFile("file", "testfile.txt", "text/plain", "File content".getBytes());

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
	void testCreateOntology_fileReadException() throws IOException {
		// Arrange
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Test Ontology");

		MultipartFile file = mock(MultipartFile.class);
		when(file.getBytes()).thenThrow(new IOException("Failed to read file"));

		Ontology ontology = new Ontology();
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(ontology);

		// Act & Assert
		assertThrows(UncheckedIOException.class, () -> {
			this.ontologyService.createOntology(dto, file);
		});
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
