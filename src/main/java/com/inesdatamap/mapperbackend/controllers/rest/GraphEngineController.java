package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.services.GraphEngineService;

/**
 * Controller to execute python commands
 */
@RestController()
@RequestMapping("/python")
public class GraphEngineController {

	private final GraphEngineService graphEngineService;

	/**
	 * Constructor
	 *
	 * @param graphEngineService
	 * 	the graph engine service
	 */
	public GraphEngineController(GraphEngineService graphEngineService) {
		this.graphEngineService = graphEngineService;
	}

	/**
	 * Run python code
	 *
	 * @return the response
	 *
	 * @throws InterruptedException
	 * 	the exception
	 */
	@GetMapping
	public ResponseEntity<List<String>> run() throws InterruptedException {

		List<String> results = graphEngineService.run();

		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

}
