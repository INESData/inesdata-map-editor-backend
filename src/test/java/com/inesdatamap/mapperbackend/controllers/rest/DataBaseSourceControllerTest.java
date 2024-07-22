package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.services.ClientDataSourceService;
import com.inesdatamap.mapperbackend.services.DataSourceService;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link DataBaseSourceController}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataBaseSourceController.class)
class DataBaseSourceControllerTest {

	@MockBean
	private ClientDataSourceService clientDataSourceService;

	@MockBean
	private DataSourceService dataSourceService;

	@Autowired
	private DataBaseSourceController controller;

	@Test
	void testGetTable() {

		// mock
		List<String> tables = Arrays.asList("table1", "table2");
		Mockito.when(clientDataSourceService.getTableNames(Mockito.anyLong(), Mockito.any())).thenReturn(tables);

		// test
		ResponseEntity<List<String>> result = controller.getTableNames(1L);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(tables, result.getBody());
	}

	@Test
	void testGetTableColumns() {

		// mock
		List<String> columns = Arrays.asList("column1", "column2");
		Mockito.when(clientDataSourceService.getColumnNames(Mockito.anyLong(), Mockito.anyString(), Mockito.any())).thenReturn(columns);

		// test
		ResponseEntity<List<String>> result = controller.getColumnNames(1L, "test");

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(columns, result.getBody());

	}

	@Test
	void testGetTableColumnsThrowsException() {

		// mock
		List<String> columns = Arrays.asList("column1", "column2");
		Mockito.when(clientDataSourceService.getColumnNames(Mockito.anyLong(), Mockito.anyString(), Mockito.any())).thenReturn(columns);

		// test and assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			controller.getColumnNames(1L, "");
		});

	}
}
