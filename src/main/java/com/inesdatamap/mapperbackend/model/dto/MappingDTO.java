package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Mapping.
 */
@Getter
@Setter
public class MappingDTO extends BaseEntityDTO {

	private String name;

	private Ontology ontology;

	private DataSource dataSource;

}
