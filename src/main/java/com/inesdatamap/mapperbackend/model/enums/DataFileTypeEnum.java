package com.inesdatamap.mapperbackend.model.enums;

/**
 * File Type enumeration
 *
 * @author gmv
 */
public enum DataFileTypeEnum {
	/**
	 * Represents a CSV file type.
	 */
	CSV("CSV"),

	/**
	 * Represents a JSON file type.
	 */
	JSON("JSON");

	/**
	 * The code representing the file type.
	 */
	private String code;

	/**
	 * Constructor for DataFileTypeEnum.
	 *
	 * @param code
	 *            the code representing the file type.
	 */
	private DataFileTypeEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

}
