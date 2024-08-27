package com.inesdatamap.mapperbackend.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.services.DataSourceService;
import com.inesdatamap.mapperbackend.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for Data Source
 */
@RestController()
@RequestMapping("/data-sources")
@Tag(name = "DataSource", description = "Manage data sources")
@Validated
public class DataSourceController {

	@Autowired
	private DataSourceService dataSourceService;

	/**
	 * Lists all data sources.
	 *
	 * @param page
	 *            page number
	 *
	 * @param size
	 *            page size
	 *
	 *
	 * @return List of all data sources
	 */
	@GetMapping(path = "")
	@Operation(summary = "List all data sources")
	public ResponseEntity<Page<DataSourceDTO>> listDataSources(@RequestParam int page, @RequestParam int size) {
		Page<DataSourceDTO> dataSources = this.dataSourceService
				.listDataSources(PageRequest.of(page, size, Sort.by(Constants.SORT_BY_NAME).ascending()));
		return ResponseEntity.ok(dataSources);
	}

	/**
	 * Deletes data source
	 *
	 * @param id
	 *            data source identifier
	 * @return status code response
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete data source")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteDataSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Data source identifier to delete", required = true) Long id) {
		this.dataSourceService.deleteDataSource(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Saves the given data source
	 *
	 * @param dataSourceDTO
	 *            to save
	 * @param file
	 *            file content to save
	 * @return saved data source
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Create given data source")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DataSourceDTO> createDataSource(
			@RequestPart("body") @Parameter(name = "dataSource", description = "The data source to save", required = true) DataSourceDTO dataSourceDTO,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		DataSourceDTO createdDataSource = this.dataSourceService.createDataSource(dataSourceDTO, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDataSource);
	}

	/**
	 * Updates the given data source
	 *
	 * @param id
	 *            data source identifier
	 * @param dataSourceDto
	 *            to update
	 * @param file
	 *            file content to update
	 * @return updated data source
	 */
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Update given data source")
	public ResponseEntity<DataSourceDTO> updateDataSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Data source identifier to update", required = true) Long id,
			@RequestPart("body") @Parameter(name = "dataSource", description = "The data source to update", required = true) DataSourceDTO dataSourceDto,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		return ResponseEntity.ok(this.dataSourceService.updateDataSource(id, dataSourceDto, file));
	}

}
