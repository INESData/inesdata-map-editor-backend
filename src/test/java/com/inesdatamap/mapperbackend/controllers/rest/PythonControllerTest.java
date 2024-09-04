package com.inesdatamap.mapperbackend.controllers.rest;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link GraphEngineController}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GraphEngineController.class)
class PythonControllerTest {

	@Autowired
	@InjectMocks
	private GraphEngineController controller;

	@Test
	void getTest() throws IOException {

		try {
			// test
			ResponseEntity<List<String>> result = controller.run();

			// verifies & asserts
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}

