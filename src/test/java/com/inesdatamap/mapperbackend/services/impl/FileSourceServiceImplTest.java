package com.inesdatamap.mapperbackend.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapper;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapperImpl;
import com.inesdatamap.mapperbackend.properties.DatasourcePathsProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link FileSourceServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DatasourcePathsProperties.class, FileSourceServiceImpl.class,
	FileSourceMapperImpl.class }, initializers = ConfigDataApplicationContextInitializer.class)
class FileSourceServiceImplTest {

	@MockBean
	private FileSourceRepository fileSourceRepository;

	@Autowired
	private FileSourceMapper fileSourceMapper;

	@Autowired
	private DatasourcePathsProperties datasourcePathsProperties;

	@Autowired
	private FileSourceServiceImpl fileSourceService;

	@Test
	void createFileSource() throws IOException {

		// Arrange
		FileSourceDTO inputDto = new FileSourceDTO();
		MultipartFile file = mock(MultipartFile.class);
		FileSource savedFileSourceEntity = new FileSource();
		savedFileSourceEntity.setId(1L);

		// Configurar un InputStream simulado para el archivo
		InputStream inputStream = new ByteArrayInputStream("data".getBytes());
		when(file.getInputStream()).thenReturn(inputStream);
		when(file.isEmpty()).thenReturn(false);
		when(file.getContentType()).thenReturn("text/csv");

		// Configuración de mocks adicionales
		when(this.fileSourceRepository.save(any())).thenReturn(savedFileSourceEntity);

		// Act
		DataSourceDTO result = this.fileSourceService.createFileSource(inputDto, file);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		verify(file, times(1)).getInputStream();
	}

	@Test
	void testUpdateFileSource() {

		// Arrange
		Long id = 1L;
		FileSourceDTO inputDto = new FileSourceDTO();
		inputDto.setId(id);
		FileSource fileSourceEntity = new FileSource();

		// Configuración de mocks
		when(this.fileSourceRepository.findById(id)).thenReturn(Optional.of(fileSourceEntity));

		// Act
		DataSourceDTO result = this.fileSourceService.updateFileSource(id, inputDto);

		// Assert
		assertNotNull(result); // Asegura que el resultado no es null
		assertEquals(inputDto.getId(), result.getId()); // Asegura que el resultado es igual al esperado
		verify(this.fileSourceRepository).findById(id);
	}

	@Test
	void testGetEntity() {
		// Mock data
		Long id = 1L;

		// Mock behavior
		Mockito.when(this.fileSourceRepository.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.fileSourceService.getEntity(id));
	}

}
