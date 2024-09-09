package com.inesdatamap.mapperbackend.services.impl;

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

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.services.GraphEngineService;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MappingServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(MockitoExtension.class)
class MappingServiceImplTest {

	@Mock
	private MappingRepository mappingRepo;

	@Mock
	private GraphEngineService graphEngineService;

	@InjectMocks
	private MappingServiceImpl mappingService;

	@Test
	void testDeleteMapping() {
		// Mock data
		Long id = 1L;
		Mapping mapping = new Mapping();

		// Mock behavior
		when(this.mappingRepo.findById(id)).thenReturn(Optional.of(mapping));

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
		when(this.mappingRepo.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.mappingService.getEntity(id));
	}

	@Test
	void testListMappings() {
		// Mock data
		Pageable pageable = PageRequest.of(0, 10);

		Mapping mapping = buildMapping();

		Page<Mapping> mappingPage = new PageImpl<>(List.of(mapping));

		// Mock behavior
		when(this.mappingRepo.findAll(pageable)).thenReturn(mappingPage);

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

	@Test
	void materializeMappingTest() {

		Mapping mapping = buildMapping();
		List<String> expectedResults = List.of("Graph", "created!");

		// Mock behavior
		when(this.mappingRepo.findById(anyLong())).thenReturn(Optional.of(mapping));
		when(this.graphEngineService.run(anyString(), anyLong(), anyList())).thenReturn(expectedResults);

		List<String> results = this.mappingService.materialize(1L);

		assertEquals(expectedResults, results);
	}

	@Test
	void materializeMappingThrowsExceptionTest() {

		Mapping mapping = buildMapping();
		List<String> expectedResults = List.of("Graph", "created!");

		// Mock behavior
		when(this.mappingRepo.findById(anyLong())).thenReturn(Optional.of(mapping));
		when(this.graphEngineService.run(anyString(), anyLong(), anyList())).thenThrow(GraphEngineException.class);

		assertThrows(GraphEngineException.class, () -> this.mappingService.materialize(1L));
	}

	private static Mapping buildMapping() {
		Mapping mapping = new Mapping();
		mapping.setId(1L);
		mapping.setName("Test Mapping");
		mapping.setRml("RML CONTENT".getBytes());

		MappingField field1 = new MappingField();
		Ontology ontology = new Ontology();
		ontology.setName("Ontology1");
		field1.setOntology(ontology);

		DataSource source = new DataSource();
		source.setName("Source1");
		field1.setSource(source);

		mapping.setFields(List.of(field1));
		return mapping;
	}
}
