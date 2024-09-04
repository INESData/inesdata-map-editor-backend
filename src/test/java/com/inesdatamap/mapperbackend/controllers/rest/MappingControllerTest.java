package com.inesdatamap.mapperbackend.controllers.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.inesdatamap.mapperbackend.services.MappingService;

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

}
