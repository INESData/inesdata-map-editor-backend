package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.services.MappingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for Mapping
 */
@RestController()
@RequestMapping("/mappings")
@Tag(name = "Mapping", description = "Manage mappings")
@Validated
public class MappingController {

	private final MappingService mappingService;

	/**
	 * This constructor initializes the controller with the provided MappingService
	 *
	 * @param mappingService
	 * 	the mapping service
	 */
	public MappingController(MappingService mappingService) {
		this.mappingService = mappingService;
	}

	/**
	 * Lists all mappings.
	 *
	 * @param page
	 * 	page number
	 * @param size
	 * 	page size
	 *
	 * @return List of all mappings
	 */
	@GetMapping(path = "")
	@Operation(summary = "List all mappings")
	public ResponseEntity<Page<SearchMappingDTO>> listMappings(@RequestParam int page, @RequestParam int size) {
		Page<SearchMappingDTO> mappings = this.mappingService.listMappings(PageRequest.of(page, size));
		return ResponseEntity.ok(mappings);
	}

	/**
	 * Creates a new mapping
	 *
	 * @param mappingDTO
	 * 	the mapping to create
	 *
	 * @return the created mapping
	 */
	@PostMapping(path = "")
	@Operation(summary = "Create a new mapping")
	public ResponseEntity<MappingDTO> create(@RequestBody @Valid MappingDTO mappingDTO) {
		Mapping mapping = this.mappingService.create(mappingDTO);
		MappingDTO body = this.mappingService.save(mapping);
		return ResponseEntity.ok(body);
	}

	/**
	 * Deletes mapping
	 *
	 * @param id
	 * 	mapping identifier
	 *
	 * @return status code response
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete mapping")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteMapping(
		@PathVariable(name = "id") @Parameter(name = "id", description = "Mapping identifier to delete", required = true) Long id) {
		this.mappingService.deleteMapping(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Materialize a mapping
	 *
	 * @param id
	 * 	the mapping identifier
	 *
	 * @return the response
	 */
	@PostMapping(path = "/{id}/materialize")
	@Operation(summary = "Materialize a mapping")
	public ResponseEntity<List<String>> materializeMapping(
		@PathVariable(name = "id") @Parameter(name = "id", description = "Mapping identifier to materialize", required = true) Long id) {
		List<String> results = this.mappingService.materialize(id);
		return ResponseEntity.ok(results);
	}

}
