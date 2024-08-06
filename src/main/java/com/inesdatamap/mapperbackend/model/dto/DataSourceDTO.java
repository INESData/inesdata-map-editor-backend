package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;
import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Data Source.
 */
@Getter
@Setter
public class DataSourceDTO extends BaseEntityDTO {

	private String name;

	private DataSourceTypeEnum type;

	private String connectionString;

	private String user;

	private String password;

	private DataBaseTypeEnum databaseType;

	private String fileName;

	private String fields;

}
