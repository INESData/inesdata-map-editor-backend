package com.inesdatamap.mapperbackend.controllers.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.services.OntologyService;
import com.inesdatamap.mapperbackend.utils.Constants;

/**
 * Unit tests for the {@link OntologyController}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OntologyController.class)
class OntologyControllerTest {

	@MockBean
	private OntologyService ontologyService;

	@Autowired
	private OntologyController controller;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testListOntologies() {

		// mock
		SearchOntologyDTO ontology1 = new SearchOntologyDTO();
		SearchOntologyDTO ontology2 = new SearchOntologyDTO();
		List<SearchOntologyDTO> ontologies = Arrays.asList(ontology1, ontology2);
		Page<SearchOntologyDTO> page = new PageImpl<>(ontologies);

		Mockito.when(this.ontologyService.listOntologies(PageRequest.of(Constants.NUMBER_0, Constants.NUMBER_10))).thenReturn(page);

		// test
		ResponseEntity<Page<SearchOntologyDTO>> result = this.controller.listOntologies(Constants.NUMBER_0, Constants.NUMBER_10);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(page, result.getBody());
	}

	@Test
	void testUpdateOntology() {

		// mock
		Long id = 1L;
		OntologyDTO searchOntologyDTO = new OntologyDTO();
		OntologyDTO updatedOntology = new OntologyDTO();

		Mockito.when(this.ontologyService.updateOntology(id, searchOntologyDTO)).thenReturn(updatedOntology);

		// test
		ResponseEntity<OntologyDTO> result = this.controller.updateOntology(id, searchOntologyDTO);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(updatedOntology, result.getBody());
	}

	@Test
	void testDeleteOntology() {

		// mock
		Long id = 1L;

		// test
		ResponseEntity<Void> result = this.controller.deleteOntology(id);

		// verifies & asserts
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

		// Verify that the service method was called once
		Mockito.verify(this.ontologyService, Mockito.times(1)).deleteOntology(id);
	}

	@Test
	void createOntology() throws Exception {
		// Arrange
		SearchOntologyDTO ontologyDto = new SearchOntologyDTO();
		ontologyDto.setId(1L);
		ontologyDto.setName("Test Ontology");

		MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				"This is the file content".getBytes());

		Mockito.when(this.ontologyService.createOntology(Mockito.any(SearchOntologyDTO.class), Mockito.any(MockMultipartFile.class)))
				.thenReturn(ontologyDto);

		// Act & Assert
		this.mockMvc
				.perform(multipart("/ontologies").file(file).param("body", "{\"id\":1,\"name\":\"Test Ontology\"}")
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Test Ontology"));
	}

}
