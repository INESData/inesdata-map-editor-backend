package com.inesdatamap.mapperbackend.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.utils.ProcessBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphEngineServiceImplTest {

	@Mock
	private AppProperties appProperties;

	@Mock
	private ProcessBuilderFactory processBuilderFactory;

	@Mock
	private ProcessBuilder processBuilder;

	@Mock
	private Process process;

	@InjectMocks
	private GraphEngineServiceImpl graphEngineService;

	@Test
	void runTest() throws IOException, InterruptedException {
		String mappingPath = "/mapping/path";
		Long mappingId = 1L;
		List<MappingField> mappingFields = List.of(new MappingField());
		List<String> expectedResult = List.of("Process", "Executed");

		InputStream stream = new ByteArrayInputStream("Process\nExecuted".getBytes());

		when(appProperties.getDataProcessingPath()).thenReturn(null);
		when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
			anyString())).thenReturn(processBuilder);
		when(processBuilder.start()).thenReturn(process);
		when(process.getInputStream()).thenReturn(stream);
		when(process.waitFor()).thenReturn(0);

		List<String> result = graphEngineService.run(mappingPath, mappingId, mappingFields);

		assertEquals(expectedResult, result);
		verify(appProperties, times(1)).getDataProcessingPath();
	}

	@Test
	void runThrowsGraphEngineExceptionOnExitCode() throws IOException, InterruptedException {
		String mappingPath = "/mapping/path";
		Long mappingId = 1L;
		List<MappingField> mappingFields = List.of(new MappingField());

		when(appProperties.getDataProcessingPath()).thenReturn(null);
		when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
			anyString())).thenReturn(processBuilder);
		when(processBuilder.start()).thenReturn(process);
		when(process.waitFor()).thenReturn(1);

		assertThrows(GraphEngineException.class, () -> {
			graphEngineService.run(mappingPath, mappingId, mappingFields);
		});

	}

	@Test
	void runThrowsGraphEngineExceptionOnIOException() throws IOException {
		String mappingPath = "valid/mapping/path";
		Long mappingId = 2L;
		List<MappingField> mappingFields = List.of(new MappingField());

		when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
			anyString())).thenReturn(processBuilder);
		when(processBuilder.start()).thenThrow(IOException.class);

		assertThrows(GraphEngineException.class, () -> {
			graphEngineService.run(mappingPath, mappingId, mappingFields);
		});
	}

	@Test
	void runThrowsGraphEngineExceptionOnInterruptedException() throws IOException, InterruptedException {
		String mappingPath = "valid/mapping/path";
		Long mappingId = 2L;
		List<MappingField> mappingFields = List.of(new MappingField());

		when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
			anyString())).thenReturn(processBuilder);
		when(processBuilder.start()).thenReturn(process);
		when(process.waitFor()).thenThrow(InterruptedException.class);

		assertThrows(GraphEngineException.class, () -> {
			graphEngineService.run(mappingPath, mappingId, mappingFields);
		});
	}

}
