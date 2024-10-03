package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapper;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapperImpl;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link FileSourceServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppProperties.class, FileSourceServiceImpl.class,
		FileSourceMapperImpl.class }, initializers = ConfigDataApplicationContextInitializer.class)
class FileSourceServiceImplTest {

	@MockBean
	private FileSourceRepository fileSourceRepository;

	@Autowired
	private FileSourceMapper fileSourceMapper;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private FileSourceServiceImpl fileSourceService;

	@Test
	void createFileSource() throws IOException {

		// Arrange
		FileSourceDTO inputDto = new FileSourceDTO();
		inputDto.setFileType(DataFileTypeEnum.CSV);

		MultipartFile file = mock(MultipartFile.class);
		FileSource savedFileSourceEntity = new FileSource();
		savedFileSourceEntity.setId(1L);

		// Config InputStream simulated for file
		InputStream inputStream = new ByteArrayInputStream("data".getBytes());
		when(file.getInputStream()).thenReturn(inputStream);
		when(file.isEmpty()).thenReturn(false);
		when(file.getContentType()).thenReturn("text/csv");
		when(file.getOriginalFilename()).thenReturn("file.csv");

		// Config additional mocks
		when(this.fileSourceRepository.save(any())).thenReturn(savedFileSourceEntity);

		// Act
		DataSourceDTO result = this.fileSourceService.createFileSource(inputDto, file);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		verify(file, times(2)).getInputStream();
	}

	@Test
	void testCreateFileSourceProcessHeadersThrowsException() throws IOException {

		MultipartFile file = mock(MultipartFile.class);
		FileSource savedFileSourceEntity = new FileSource();
		FileSourceDTO inputDto = new FileSourceDTO();

		// Config InputStream simulated for file
		InputStream inputStream = new ByteArrayInputStream("data".getBytes());
		when(file.getInputStream()).thenReturn(inputStream);
		when(file.isEmpty()).thenReturn(false);
		when(file.getContentType()).thenReturn("text/csv");

		// Config additional mocks
		when(this.fileSourceRepository.save(any())).thenReturn(savedFileSourceEntity);
		when(FileUtils.processFileHeaders(file)).thenThrow(new IOException());

		assertThrows(FileCreationException.class, () -> this.fileSourceService.createFileSource(inputDto, file));

	}

	@Test
	void testCreateFileSourceThrowsFileCreationException() throws IOException {

		MultipartFile file = mock(MultipartFile.class);
		FileSource savedFileSourceEntity = new FileSource();
		savedFileSourceEntity.setId(1L);

		// Config InputStream simulated for file
		FileSourceDTO inputDto = new FileSourceDTO();
		inputDto.setFileType(DataFileTypeEnum.CSV);

		InputStream inputStream = new ByteArrayInputStream("data".getBytes());
		when(file.getInputStream()).thenReturn(inputStream);
		when(file.isEmpty()).thenReturn(false);
		when(file.getContentType()).thenReturn("text/csv");
		when(file.getOriginalFilename()).thenReturn("file.csv");

		// Config additional mocks
		when(this.fileSourceRepository.save(any())).thenReturn(savedFileSourceEntity);

		doThrow(IOException.class).when(file).transferTo(any(File.class));

		assertThrows(FileCreationException.class, () -> this.fileSourceService.createFileSource(inputDto, file));

	}

	@Test
	void testUpdateFileSource() {

		// Arrange
		Long id = 1L;
		FileSourceDTO inputDto = new FileSourceDTO();
		inputDto.setId(id);
		FileSource fileSourceEntity = new FileSource();

		// Mock config
		when(this.fileSourceRepository.findById(id)).thenReturn(Optional.of(fileSourceEntity));

		// Act
		DataSourceDTO result = this.fileSourceService.updateFileSource(id, inputDto);

		// Assert
		assertNotNull(result);
		assertEquals(inputDto.getId(), result.getId());
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

	@Test
	void testGetFileSourceByType() {

		// Arrange
		String fileType = "CSV";
		DataFileTypeEnum typeEnum = DataFileTypeEnum.valueOf(fileType);

		List<FileSource> fileSources = List.of(new FileSource());
		when(this.fileSourceRepository.findByFileTypeOrderByNameAsc(typeEnum)).thenReturn(fileSources);

		// Act
		List<FileSourceDTO> result = this.fileSourceService.getFileSourceByType(fileType);

		// Assert
		assertNotNull(result);
		verify(this.fileSourceRepository).findByFileTypeOrderByNameAsc(typeEnum);
	}

	@Test
	void testGetFileFields() {

		// Valid fields and delimited by ,
		Long idWithValidFields = 1L;
		String fieldsWithValid = "field1,field2,field3";
		FileSource fileSourceWithValidFields = new FileSource();
		fileSourceWithValidFields.setFields(fieldsWithValid);
		when(this.fileSourceRepository.findById(idWithValidFields)).thenReturn(Optional.of(fileSourceWithValidFields));

		// Empty fields
		Long idWithEmptyFields = 2L;
		String fieldsWithEmpty = "";
		FileSource fileSourceWithEmptyFields = new FileSource();
		fileSourceWithEmptyFields.setFields(fieldsWithEmpty);
		when(this.fileSourceRepository.findById(idWithEmptyFields)).thenReturn(Optional.of(fileSourceWithEmptyFields));

		// Fields are null
		Long idWithNullFields = 3L;
		FileSource fileSourceWithNullFields = new FileSource();
		fileSourceWithNullFields.setFields(null);
		when(this.fileSourceRepository.findById(idWithNullFields)).thenReturn(Optional.of(fileSourceWithNullFields));

		// Act and Assert for valid fields
		List<String> resultWithValidFields = this.fileSourceService.getFileFields(idWithValidFields);
		List<String> expectedWithValidFields = Arrays.asList("field1", "field2", "field3");
		assertEquals(expectedWithValidFields, resultWithValidFields);

		// Act and Assert for empty fields
		List<String> resultWithEmptyFields = this.fileSourceService.getFileFields(idWithEmptyFields);
		assertTrue(resultWithEmptyFields.isEmpty());

		// Act and Assert for null fields
		List<String> resultWithNullFields = this.fileSourceService.getFileFields(idWithNullFields);
		assertTrue(resultWithNullFields.isEmpty());
	}

	@Test
	void testGetFileAttributes() throws Exception {

		// Arrange
		Long id = 1L;
		FileSource fileSource = new FileSource();
		fileSource.setFilePath("src/test/resources/xml");
		fileSource.setFileName("oli_doliva_en_textures_clean.xml");

		// Mock the repository to return the FileSource
		when(this.fileSourceRepository.findById(id)).thenReturn(Optional.of(fileSource));

		// Create a real test file (make sure the file exists in your resources)
		File file = new File(this.getClass().getClassLoader().getResource("xml/oli_doliva_en_textures_clean.xml").getFile());
		assertTrue(file.exists(), "Test file should exist");

		// Create the mock XMLStreamReader
		XMLStreamReader xmlStreamReader = mock(XMLStreamReader.class);

		// Simulate the behavior of the XMLStreamReader
		when(xmlStreamReader.hasNext()).thenReturn(true, true, false);
		when(xmlStreamReader.next()).thenReturn(XMLStreamReader.START_ELEMENT, XMLStreamReader.END_ELEMENT);
		when(xmlStreamReader.getAttributeCount()).thenReturn(1);
		when(xmlStreamReader.getAttributeLocalName(0)).thenReturn("attributeName");

		// Mock the XMLInputFactory to return the mock XMLStreamReader
		XMLInputFactory xmlInputFactory = mock(XMLInputFactory.class);
		when(xmlInputFactory.createXMLStreamReader(new FileInputStream(file))).thenReturn(xmlStreamReader);

		// Act
		List<String> result = this.fileSourceService.getFileAttributes(id);

		// Assert
		assertNotNull(result);

		// Verify that the findById method of the repository was called
		verify(this.fileSourceRepository).findById(id);
	}

	@Test
	void testExtractAttributes() throws Exception {

		// Arrange
		XMLStreamReader reader = mock(XMLStreamReader.class);
		Set<String> attributes = new HashSet<>();

		// Simulate the behavior of the XMLStreamReader
		when(reader.hasNext()).thenReturn(true, true, true, false);
		when(reader.next()).thenReturn(XMLStreamConstants.START_ELEMENT, XMLStreamConstants.CHARACTERS, XMLStreamConstants.END_ELEMENT);

		// Simulate the start of an element with an attribute
		when(reader.getLocalName()).thenReturn("root/lexicalData/lexConcept");
		when(reader.getAttributeCount()).thenReturn(1);
		when(reader.getAttributeLocalName(0)).thenReturn("conceptID");
		when(reader.getAttributeValue(0)).thenReturn("attributeValue");

		// Simulate the character node text content
		when(reader.getText()).thenReturn("someValue");

		// Act
		FileSourceServiceImpl.extractAttributes(reader, attributes);

		// Assert
		assertTrue(attributes.contains("root/lexicalData/lexConcept/@conceptID"));
	}

	@Test
	void testProcessStartElement() throws Exception {
		// Arrange
		XMLStreamReader reader = mock(XMLStreamReader.class);
		StringBuilder currentPath = new StringBuilder();
		Set<String> attributes = new HashSet<>();

		// Simulate the behavior of the XMLStreamReader
		when(reader.getLocalName()).thenReturn("root/lexicalData/lexConcept");
		when(reader.getAttributeCount()).thenReturn(2);
		when(reader.getAttributeLocalName(0)).thenReturn("conceptID");
		when(reader.getAttributeValue(0)).thenReturn("attributeValue");

		// Act
		boolean isLeafNode = FileSourceServiceImpl.processStartElement(reader, currentPath, attributes);

		// Assert
		// Verify that the current path is updated correctly
		assertEquals("root/lexicalData/lexConcept", currentPath.toString());

		// Verify that the method returns true (indicating a potential leaf node)
		assertTrue(isLeafNode);
	}

	@Test
	void testProcessCharacters() throws Exception {
		// Arrange
		XMLStreamReader reader = mock(XMLStreamReader.class);
		StringBuilder currentPath = new StringBuilder("root/leafElement");
		Set<String> attributes = new HashSet<>();
		boolean isLeafNode = true;

		// Simulate the behavior of the XMLStreamReader
		when(reader.getText()).thenReturn("leafValue");

		// Act
		FileSourceServiceImpl.processCharacters(reader, currentPath, attributes, isLeafNode);

		// Assert
		// Verify that the leaf node's path is added to the attributes set
		assertTrue(attributes.contains("root/leafElement"));
	}

	@Test
	void testRemoveLastElementFromXPath_NonEmptyPath() {
		// Case 1: Path with n elements
		StringBuilder currentPath1 = new StringBuilder("root/lexicalData/lexConcept");
		FileSourceServiceImpl.removeLastElementFromXPath(currentPath1);
		assertEquals("root/lexicalData", currentPath1.toString(), "Failed for non-empty path case.");

		// Case 2: No slashes path
		StringBuilder currentPath2 = new StringBuilder("leafElement");
		FileSourceServiceImpl.removeLastElementFromXPath(currentPath2);
		assertEquals("", currentPath2.toString(), "Failed for single element path case.");
	}

	@Test
	void testRemoveLastElementFromXPath_EmptyPath() {
		// Case 3: Empty path
		StringBuilder currentPath3 = new StringBuilder("");
		FileSourceServiceImpl.removeLastElementFromXPath(currentPath3);
		assertEquals("", currentPath3.toString(), "Failed for empty path case.");
	}

}
