package com.inesdatamap.mapperbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a LogicalTable.
 */
@Getter
@Setter
public class LogicalTableDTO extends BaseEntityDTO {

	private String tableName;
	private String query;

}
