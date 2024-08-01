package com.inesdatamap.mapperbackend.controllers.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.services.OntologyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for Ontology
 */
@RestController()
@RequestMapping("/ontologies")
@Tag(name = "Ontology", description = "Manage ontologies")
@Validated
public class OntologyController {

	private OntologyService ontologyService;

	/**
	 * This constructor initializes the controller with the provided OntologyService
	 *
	 * @param ontologyService
	 *            ontology service
	 */
	public OntologyController(OntologyService ontologyService) {
		this.ontologyService = ontologyService;
	}

	/**
	 * Lists all ontologies.
	 *
	 * @param page
	 *            page number
	 *
	 * @param size
	 *            page size
	 *
	 * @return List of all ontologies
	 */
	@GetMapping(path = "")
	@Operation(summary = "List all ontologies")
	public ResponseEntity<Page<SearchOntologyDTO>> listOntologies(@RequestParam int page, @RequestParam int size) {
		Page<SearchOntologyDTO> ontologies = this.ontologyService.listOntologies(PageRequest.of(page, size));
		return ResponseEntity.ok(ontologies);
	}

	/**
	 * Updates the given ontology
	 *
	 * @param id
	 *            ontology identifier
	 * @param ontologyDto
	 *            to update
	 * @return updated ontology
	 */
	@PutMapping(value = "/{id}")
	@Operation(summary = "Update given ontology")
	public ResponseEntity<OntologyDTO> updateOntology(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier to update", required = true) Long id,
			@Valid @RequestBody @Parameter(name = "ontology", description = "The ontology to update", required = true) OntologyDTO ontologyDto) {
		return ResponseEntity.ok(this.ontologyService.updateOntology(id, ontologyDto));
	}

	/**
	 * Deletes ontology
	 *
	 * @param id
	 *            ontology identifier
	 * @return status code response
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete ontology")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteOntology(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier to delete", required = true) Long id) {
		this.ontologyService.deleteOntology(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Saves the given ontology
	 *
	 * @param ontologyDto
	 *            to save
	 * @param file
	 *            file content to save
	 * @return saved ontology
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Create given ontology")
	public ResponseEntity<SearchOntologyDTO> createOntology(
			@RequestPart("body") @Parameter(name = "ontology", description = "The ontology to save", required = true) SearchOntologyDTO ontologyDto,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		return ResponseEntity.ok(this.ontologyService.createOntology(ontologyDto, file));
	}
}
