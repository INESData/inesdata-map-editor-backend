package com.inesdatamap.mapperbackend.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.services.GraphEngineService;

/**
 * Graph engine service implementation
 */
@Service
public class GraphEngineImpl implements GraphEngineService {

	/**
	 * Logger
	 */
	protected final Log LOGGER = LogFactory.getLog(this.getClass());

	@Override
	public List<String> run(String rml) {

		ProcessBuilder processBuilder = new ProcessBuilder("python", "-m", "kg_generation", "--help");
		processBuilder.redirectErrorStream(true);
		List<String> results;

		try {

			Process process = processBuilder.start();

			results = readProcessOutput(process.getInputStream());

			int exitCode = process.waitFor();
			LOGGER.info("ExitCode: " + exitCode);

			if (exitCode != 0) {
				throw new GraphEngineException(results.toString(), null);
			}

		} catch (IOException e) {
			throw new GraphEngineException("GraphEngine error", e);
		} catch (InterruptedException e) {
			LOGGER.warn("Thread interrupted!");
			// Restore interrupted state...
			Thread.currentThread().interrupt();
			throw new GraphEngineException("GraphEngine error", e);
		}

		return results;
	}

	private static List<String> readProcessOutput(InputStream inputStream) throws IOException {
		try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
			return output.lines().toList();
		}
	}

}
