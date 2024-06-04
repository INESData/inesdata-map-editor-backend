package com.inesdatamap.mapperbackend.controllers;

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

import com.inesdatamap.mapperbackend.controllers.rest.PythonController;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link PythonController}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PythonController.class)
class PythonControllerTest {

	@Autowired
	@InjectMocks
	private PythonController controller;

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

