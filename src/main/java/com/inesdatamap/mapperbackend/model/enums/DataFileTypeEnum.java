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
	CSV("CSV", "text/csv"),

	/**
	 * Represents a JSON file type.
	 */
	JSON("JSON", "application/json");

	/**
	 * The code representing the file type.
	 */
	private String code;

	/**
	 * The MIME type representing the file type.
	 */
	private final String mimeType;

	/**
	 * Constructor for DataFileTypeEnum.
	 *
	 * @param code
	 * 	the code representing the file type.
	 * @param mimeType
	 * 	the MIME type representing the file type.
	 */
	private DataFileTypeEnum(String code, String mimeType) {
		this.code = code;
		this.mimeType = mimeType;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return the MIME type representing the file type.
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * Validate if the file mimeType is valid according to the enum.
	 *
	 * @param mimeType
	 * 	The file mimeType to validate.
	 *
	 * @return true if the mimeType is valid, false otherwise.
	 */
	public static boolean isValidFile(String mimeType) {
		for (DataFileTypeEnum type : values()) {
			if (type.getMimeType().equalsIgnoreCase(mimeType)) {
				return true;
			}
		}
		return false;
	}

}
