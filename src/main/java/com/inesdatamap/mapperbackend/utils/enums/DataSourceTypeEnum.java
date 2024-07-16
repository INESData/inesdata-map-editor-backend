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
	DATABASE("DATABASE"),

	/**
	 * Represents a CSV data source.
	 */
	CSV("CSV");

	/**
	 * The code representing the database type.
	 */
	private String code;

	/**
	 * Constructor for DataSourceTypeEnum.
	 *
	 * @param code
	 *            the code representing the data source type.
	 */
	private DataSourceTypeEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

}
