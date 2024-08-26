package com.inesdatamap.mapperbackend.controllers.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.services.MappingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for Mapping
 */
@RestController()
@RequestMapping("/mappings")
@Tag(name = "Mapping", description = "Manage mappings")
@Validated
public class MappingController {

	private MappingService mappingService;

	/**
	 * This constructor initializes the controller with the provided MappingService
	 *
	 * @param mappingService
	 *            mappingService
	 */
	public MappingController(MappingService mappingService) {
		this.mappingService = mappingService;
	}

	/**
	 * Lists all mappings.
	 *
	 * @param page
	 *            page number
	 *
	 * @param size
	 *            page size
	 *
	 * @return List of all mappings
	 */
	@GetMapping(path = "")
	@Operation(summary = "List all mappings")
	public ResponseEntity<Page<MappingDTO>> listMappings(@RequestParam int page, @RequestParam int size) {
		Page<MappingDTO> mappings = this.mappingService.listMappings(PageRequest.of(page, size));
		return ResponseEntity.ok(mappings);
	}

}
