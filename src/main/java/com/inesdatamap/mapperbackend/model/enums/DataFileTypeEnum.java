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
	 *            the code representing the file type.
	 * @param mimeType
	 *            the MIME type representing the file type.
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
	 * Validate if the file extension is valid according to the enum.
	 *
	 * @param extension
	 *            The file extension to validate.
	 * @return true if the extension is valid, false otherwise.
	 */
	public static boolean isValidExtension(String extension) {
		for (DataFileTypeEnum type : values()) {
			if (type.getMimeType().equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

}
