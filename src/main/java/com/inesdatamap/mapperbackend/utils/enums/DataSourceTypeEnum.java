package com.inesdatamap.mapperbackend.utils.enums;

/**
 * Data Source Type enumeration
 *
 * @author gmv
 *
 */
public enum DataSourceTypeEnum {
	/**
	 * Represents a database data source.
	 */
	DATABASE("datasource.type.database"),

	/**
	 * Represents a CSV data source.
	 */
	CSV("datasource.type.csv");

	private String code;

	private DataSourceTypeEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return this.code;
	}

}
