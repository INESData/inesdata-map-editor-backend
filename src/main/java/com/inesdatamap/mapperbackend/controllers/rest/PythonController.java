package com.inesdatamap.mapperbackend.controllers.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to execute python commands
 */
@RestController()
@RequestMapping("/python")
public class PythonController {

	/**
	 * Logger
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Run python code
	 *
	 * @return the response
	 *
	 * @throws InterruptedException
	 * 		the exception
	 * @throws IOException
	 * 		the other exception
	 */
	@GetMapping
	public ResponseEntity<List<String>> run() throws InterruptedException, IOException {

		ProcessBuilder processBuilder = new ProcessBuilder("python", "-m", "inesdata_mov_datasets", "--help");
		processBuilder.redirectErrorStream(true);
		List<String> results;

		Process process = processBuilder.start();

		results = readProcessOutput(process.getInputStream());

		int exitCode = process.waitFor();
		logger.info("ExitCode: " + exitCode);

		if (exitCode != 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(results);
		}

		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	private static List<String> readProcessOutput(InputStream inputStream) throws IOException {
		try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
			return output.lines().toList();
		}
	}

}
