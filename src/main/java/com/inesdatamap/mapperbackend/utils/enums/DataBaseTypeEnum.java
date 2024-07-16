
package com.inesdatamap.mapperbackend.utils.enums;

/**
 * Data Base Type enumeration
 *
 * @author gmv
 *
 */
public enum DataBaseTypeEnum {
	/**
	 * Represents a PostgreSQL database type.
	 */
	POSTGRESQL("POSTGRESQL"),

	/**
	 * Represents a MySQL database type.
	 */
	MYSQL("MYSQL"),

	/**
	 * Represents a MongoDB database type.
	 */
	MONGO("MONGO");

	/**
	 * The code representing the database type.
	 */
	private String code;

	/**
	 * Constructor for DataBaseTypeEnum.
	 *
	 * @param code
	 *            the code representing the database type.
	 */
	private DataBaseTypeEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

}
