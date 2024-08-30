package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a MappingField.
 */
@Getter
@Setter
public class MappingFieldDTO {

	private LogicalTableDTO logicalTable;
	private LogicalSourceDTO logicalSource;
	private SubjectMapDTO subject;
	private List<PredicateObjectMapDTO> predicates;
	private DataSourceDTO source;
	private OntologyDTO ontology;

}
