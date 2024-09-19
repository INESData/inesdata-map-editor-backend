package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.services.MappingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MappingController}
 *
 * @author gmv
 */
@ContextConfiguration(classes = MappingController.class)
@WebMvcTest(MappingController.class)
class MappingControllerTest {

	@Autowired
	private MappingController mappingController;

	@MockBean
	private MappingService mappingService;

	@Test
	void testDeleteOntology() {

		// mock
		Long id = 1L;

		// test
		ResponseEntity<Void> result = this.mappingController.deleteMapping(id);

		// Verifies & asserts
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

		// Verify that the service method was called once
		Mockito.verify(this.mappingService, Mockito.times(1)).deleteMapping(id);
	}

	@Test
	void testListMappings() {
		int page = 0;
		int size = 10;
		Page<SearchMappingDTO> expectedMappings = new PageImpl<>(List.of(new SearchMappingDTO()));

		when(mappingService.listMappings(PageRequest.of(page, size))).thenReturn(expectedMappings);

		ResponseEntity<Page<SearchMappingDTO>> response = mappingController.listMappings(page, size);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedMappings, response.getBody());
	}

	@Test
	void testCreateMapping() {
		MappingDTO mappingDTO = new MappingDTO();
		MappingDTO createdMapping = new MappingDTO();

		when(mappingService.save(any())).thenReturn(createdMapping);

		ResponseEntity<MappingDTO> response = mappingController.create(mappingDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(createdMapping, response.getBody());
	}

	@Test
	void testMaterializeMapping() {
		Long id = 1L;
		List<String> expectedResults = List.of("result1", "result2");

		when(mappingService.materialize(id)).thenReturn(expectedResults);

		ResponseEntity<Void> response = mappingController.materializeMapping(id);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

	}
}
