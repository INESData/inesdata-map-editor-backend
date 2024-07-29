package com.inesdatamap.mapperbackend.controllers.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.services.OntologyService;
import com.inesdatamap.mapperbackend.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
	 *
	 * @return List of all ontologies
	 */
	@GetMapping(path = "")
	@Operation(summary = "List all ontologies")
	@ApiResponse(responseCode = "200", description = "Succes", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OntologyDTO.class))) })
	public ResponseEntity<Page<OntologyDTO>> listOntologies() {
		Page<OntologyDTO> ontologies = this.ontologyService.listOntologies(PageRequest.of(Constants.NUMBER_0, Constants.NUMBER_10));
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
	@ApiResponse(responseCode = "200", description = "Sucess", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OntologyDTO.class)) })
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
	@ApiResponse(responseCode = "204", description = "Deleted", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OntologyDTO.class))) })
	public ResponseEntity<Void> deleteOntology(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier to delete", required = true) Long id) {
		this.ontologyService.deleteOntology(id);
		return ResponseEntity.noContent().build();
	}

}
