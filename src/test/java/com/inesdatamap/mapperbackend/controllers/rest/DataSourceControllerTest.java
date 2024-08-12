package com.inesdatamap.mapperbackend.controllers.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.services.DataSourceService;
import com.inesdatamap.mapperbackend.utils.Constants;

/**
 * Unit tests for the {@link DataSourceController}
 *
 * @author gmv
 */
@ContextConfiguration(classes = DataSourceController.class)
@WebMvcTest(DataSourceController.class)
class DataSourceControllerTest {

	@MockBean
	private DataSourceService dataSourceService;

	@Autowired
	private DataSourceController controller;

	@Test
	void testListDataSources() throws Exception {

		// mock
		DataSourceDTO dataSourceDTO1 = new DataSourceDTO();
		DataSourceDTO dataSourceDTO2 = new DataSourceDTO();
		List<DataSourceDTO> dataSources = Arrays.asList(dataSourceDTO1, dataSourceDTO2);
		Page<DataSourceDTO> page = new PageImpl<>(dataSources);

		Mockito.when(this.dataSourceService.listDataSources(PageRequest.of(Constants.NUMBER_0, Constants.NUMBER_10))).thenReturn(page);

		// test
		ResponseEntity<Page<DataSourceDTO>> result = this.controller.listDataSources(Constants.NUMBER_0, Constants.NUMBER_10);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(page, result.getBody());
	}

	@Test
	void testDeleteOntology() {

		// mock
		Long id = 1L;

		// test
		ResponseEntity<Void> result = this.controller.deleteDataSource(id);

		// Verifies & asserts
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

		// Verify that the service method was called once
		Mockito.verify(this.dataSourceService, Mockito.times(1)).deleteDataSource(id);
	}

	@Test
	void testCreateDataSource() throws Exception {

		// Mock data
		DataSourceDTO dataSourceDto = new DataSourceDTO();
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "header1,header2".getBytes());

		// Mock the service method call
		DataSourceDTO createdDataSourceDto = new DataSourceDTO();
		Mockito.when(this.dataSourceService.createDataSource(dataSourceDto, file)).thenReturn(createdDataSourceDto);

		// Test the controller method
		ResponseEntity<DataSourceDTO> result = this.controller.createDataSource(dataSourceDto, file);

		// Verifies & asserts
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals(createdDataSourceDto, result.getBody());

		// Verify that the service method was called once with correct parameters
		Mockito.verify(this.dataSourceService, Mockito.times(1)).createDataSource(dataSourceDto, file);
	}

	@Test
	void testUpdateDataSource() throws Exception {

		// Mock data
		Long id = 1L;
		DataSourceDTO dataSourceDto = new DataSourceDTO();
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "header1,header2".getBytes());

		// Mock the service method call
		DataSourceDTO updatedDataSourceDto = new DataSourceDTO();
		Mockito.when(this.dataSourceService.updateDataSource(id, dataSourceDto, file)).thenReturn(updatedDataSourceDto);

		// Test the controller method
		ResponseEntity<DataSourceDTO> result = this.controller.updateDataSource(id, dataSourceDto, file);

		// Verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(updatedDataSourceDto, result.getBody());

		// Verify that the service method was called once with correct parameters
		Mockito.verify(this.dataSourceService, Mockito.times(1)).updateDataSource(id, dataSourceDto, file);
	}

}
