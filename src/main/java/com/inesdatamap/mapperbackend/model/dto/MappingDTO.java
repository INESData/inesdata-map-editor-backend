package com.inesdatamap.mapperbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Mapping.
 */
@Getter
@Setter
public class MappingDTO extends BaseEntityDTO {

	private String name;

	private OntologyDTO ontology;

	private DataSourceDTO dataSource;

}
