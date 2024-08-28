package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link FileSourceServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(MockitoExtension.class)
class FileSourceServiceImplTest {

	@Mock
	private FileSourceMapper fileSourceMapper;

	@Mock
	private FileSourceRepository fileSourceRepository;

	@Mock
	private FileUtils fileUtils;

	@InjectMocks
	private FileSourceServiceImpl fileSourceService;

	@Test
	void createFileSource() throws IOException {
		// Inicializar los mocks
		MockitoAnnotations.openMocks(this);

		// Arrange
		FileSourceDTO inputDto = new FileSourceDTO();
		MultipartFile file = mock(MultipartFile.class);
		FileSource fileSourceEntity = new FileSource();
		FileSource savedFileSourceEntity = new FileSource();
		FileSourceDTO outputDto = new FileSourceDTO();

		// Configurar un InputStream simulado para el archivo
		InputStream inputStream = new ByteArrayInputStream("data".getBytes());
		when(file.getInputStream()).thenReturn(inputStream);
		when(file.isEmpty()).thenReturn(false);
		when(file.getContentType()).thenReturn("text/csv");

		// Configuración de mocks adicionales
		when(this.fileSourceMapper.dtoToEntity(inputDto)).thenReturn(fileSourceEntity);
		when(this.fileSourceRepository.save(fileSourceEntity)).thenReturn(savedFileSourceEntity);
		when(this.fileSourceMapper.toDTO(savedFileSourceEntity)).thenReturn(outputDto);

		// Act
		DataSourceDTO result = this.fileSourceService.createFileSource(inputDto, file);

		// Assert
		assertNotNull(result);
		assertEquals(outputDto, result);
		verify(this.fileSourceMapper).dtoToEntity(inputDto);
		verify(this.fileSourceRepository).save(fileSourceEntity);
		verify(this.fileSourceMapper).toDTO(savedFileSourceEntity);
		verify(file, times(1)).getInputStream();
	}

	@Test
	void testUpdateFileSource() {
		// Inicializar los mocks
		MockitoAnnotations.openMocks(this);

		// Arrange
		Long id = 1L;
		FileSourceDTO inputDto = new FileSourceDTO();
		FileSource fileSourceEntity = new FileSource();
		FileSourceDTO outputDto = new FileSourceDTO();

		// Configuración de mocks
		when(this.fileSourceRepository.findById(id)).thenReturn(Optional.of(fileSourceEntity));
		doNothing().when(this.fileSourceMapper).merge(inputDto, fileSourceEntity);
		when(this.fileSourceMapper.toDTO(fileSourceEntity)).thenReturn(outputDto);

		// Act
		DataSourceDTO result = this.fileSourceService.updateFileSource(id, inputDto);

		// Assert
		assertNotNull(result); // Asegura que el resultado no es null
		assertEquals(outputDto, result); // Asegura que el resultado es igual al esperado
		verify(this.fileSourceRepository).findById(id);
		verify(this.fileSourceMapper).merge(inputDto, fileSourceEntity);
		verify(this.fileSourceMapper).toDTO(fileSourceEntity);
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
