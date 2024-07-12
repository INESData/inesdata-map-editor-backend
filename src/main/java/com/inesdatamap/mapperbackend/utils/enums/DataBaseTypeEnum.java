
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
	POSTGRESQL("database.type.postgresql"),

	/**
	 * Represents a MySQL database type.
	 */
	MYSQL("database.type.mysql"),

	/**
	 * Represents a MongoDB database type.
	 */
	MONGO("database.type.mongo");

	private String code;

	private DataBaseTypeEnum(String code) {
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
