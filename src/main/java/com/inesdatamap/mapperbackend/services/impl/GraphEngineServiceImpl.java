package com.inesdatamap.mapperbackend.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.services.GraphEngineService;
import com.inesdatamap.mapperbackend.utils.Constants;
import com.inesdatamap.mapperbackend.utils.ProcessBuilderFactory;

/**
 * Graph engine service implementation
 */
@Service
public class GraphEngineServiceImpl implements GraphEngineService {

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private ProcessBuilderFactory processBuilderFactory;

	/**
	 * Logger
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public List<String> run(String mappingPath, Long mappingId, List<MappingField> mappingFields) {

		List<String> results;

		String outputDir = String.join(File.separator, appProperties.getDataProcessingPath(), Constants.DATA_OUTPUT_FOLDER_NAME,
			mappingId.toString(), Constants.KG_OUTPUT_FILE_NAME);

		// Run the graph engine
		results = startProcess(mappingPath, outputDir);

		return results;
	}

	private List<String> startProcess(String mappingPath, String outputDir) {

		List<String> results;

		try {

			// Create the output directory
			Files.createDirectories(Paths.get(outputDir).getParent());

			String[] commands = new String[] { "python", "-m", "kg_generation", "-m", mappingPath, "-o", outputDir };

			ProcessBuilder processBuilder = processBuilderFactory.createProcessBuilder(commands);

			Process process = processBuilder.start();
			results = readProcessOutput(process.getInputStream());

			int exitCode = process.waitFor();
			logger.info("ExitCode: " + exitCode);

			if (exitCode != 0) {
				throw new GraphEngineException(results.toString(), null);
			}

		} catch (IOException e) {
			throw new GraphEngineException("GraphEngine error", e);
		} catch (InterruptedException e) {
			logger.warn("Thread interrupted!");
			// Restore interrupted state...
			Thread.currentThread().interrupt();
			throw new GraphEngineException("GraphEngine error", e);
		}

		return results;
	}

	private static List<String> readProcessOutput(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return List.of();
		}
		try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
			return output.lines().toList();
		}
	}

}
