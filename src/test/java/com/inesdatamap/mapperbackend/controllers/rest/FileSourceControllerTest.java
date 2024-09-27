package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.services.FileSourceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link FileSourceController}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FileSourceController.class)
class FileSourceControllerTest {

	@MockBean
	private FileSourceService fileSourceService;

	@Autowired
	private FileSourceController fileSourceController;

	@Test
	void testUpdateFileSource() {

		DataSourceDTO dataSourceDTO = new DataSourceDTO();
		FileSourceDTO fileSourceDTO = new FileSourceDTO();

		Long id = 1L;
		// Mock the service call
		when(this.fileSourceService.updateFileSource(Mockito.eq(id), Mockito.any(FileSourceDTO.class))).thenReturn(dataSourceDTO);

		// Test the controller method
		ResponseEntity<DataSourceDTO> result = this.fileSourceController.updateFileSource(id, fileSourceDTO);

		// Verify and assert
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(dataSourceDTO, result.getBody());
	}

	@Test
	void testCreateFileSource() {

		// Arrange
		DataSourceDTO dataSourceDTO = new DataSourceDTO();
		FileSourceDTO fileSourceDTO = new FileSourceDTO();
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "sample data".getBytes());

		// Mock the service call
		when(this.fileSourceService.createFileSource(Mockito.any(FileSourceDTO.class), Mockito.eq(file))).thenReturn(dataSourceDTO);

		// Test the controller method
		ResponseEntity<DataSourceDTO> result = this.fileSourceController.createFileSource(fileSourceDTO, file);

		// Verify and assert
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals(dataSourceDTO, result.getBody());
	}

	@Test
	void testGetFileSourceByType() {
		// Arrange
		String fileType = "CSV";
		FileSourceDTO fileSourceDTO1 = new FileSourceDTO();
		fileSourceDTO1.setId(1L);

		FileSourceDTO fileSourceDTO2 = new FileSourceDTO();
		fileSourceDTO2.setId(2L);

		List<FileSourceDTO> fileSourceDTOList = Arrays.asList(fileSourceDTO1, fileSourceDTO2);

		// Mock the service call
		when(this.fileSourceService.getFileSourceByType(fileType)).thenReturn(fileSourceDTOList);

		// Act
		ResponseEntity<List<FileSourceDTO>> result = this.fileSourceController.getFileSourceByType(fileType);

		// Assert
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(fileSourceDTOList, result.getBody());
	}

}
