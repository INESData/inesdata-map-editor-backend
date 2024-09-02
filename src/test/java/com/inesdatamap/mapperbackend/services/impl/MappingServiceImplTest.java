package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link MappingServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(MockitoExtension.class)
class MappingServiceImplTest {

	@Mock
	private MappingRepository mappingRepo;

	@InjectMocks
	private MappingServiceImpl mappingService;

	@Test
	void testDeleteMapping() {
		// Mock data
		Long id = 1L;
		Mapping mapping = new Mapping();

		// Mock behavior
		Mockito.when(this.mappingRepo.findById(id)).thenReturn(Optional.of(mapping));

		// Test
		this.mappingService.deleteMapping(id);

		// Verify
		Mockito.verify(this.mappingRepo, Mockito.times(1)).deleteById(id);
	}

	@Test
	void testGetEntity() {
		// Mock data
		Long id = 1L;

		// Mock behavior
		Mockito.when(this.mappingRepo.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.mappingService.getEntity(id));
	}

	@Test
	void testListMappings() {
		// Mock data
		Pageable pageable = PageRequest.of(0, 10);

		Mapping mapping = new Mapping();
		mapping.setId(1L);
		mapping.setName("Test Mapping");

		MappingField field1 = new MappingField();
		Ontology ontology = new Ontology();
		ontology.setName("Ontology1");
		field1.setOntology(ontology);

		DataSource source = new DataSource();
		source.setName("Source1");
		field1.setSource(source);

		mapping.setFields(List.of(field1));

		Page<Mapping> mappingPage = new PageImpl<>(List.of(mapping));

		// Mock behavior
		Mockito.when(this.mappingRepo.findAll(pageable)).thenReturn(mappingPage);

		// Test
		Page<SearchMappingDTO> resultPage = this.mappingService.listMappings(pageable);

		// Verify
		assertEquals(1, resultPage.getTotalElements());
		SearchMappingDTO resultDto = resultPage.getContent().get(0);
		assertEquals(1L, resultDto.getId());
		assertEquals("Test Mapping", resultDto.getName());
		assertEquals(List.of("Ontology1"), resultDto.getOntologies());
		assertEquals(List.of("Source1"), resultDto.getDataSources());
	}

}
