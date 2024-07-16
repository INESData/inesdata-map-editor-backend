package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Tabular Source db entity representation
 *
 * @author gmv
 *
 */
@Entity
public class TabularSource extends DataSource implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8435966261102175199L;

	/**
	 * The name of the file.
	 */
	@Column(name = "file_name")
	private String fileName;

	/**
	 * The path in MinIO storage.
	 */
	@Column(name = "minio_path")
	private String minioPath;

	/**
	 * The fields of the file or data.
	 */
	@Column(name = "fields")
	private String fields;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the minioPath
	 */
	public String getMinioPath() {
		return this.minioPath;
	}

	/**
	 * @param minioPath
	 *            the minioPath to set
	 */
	public void setMinioPath(String minioPath) {
		this.minioPath = minioPath;
	}

	/**
	 * @return the fields
	 */
	public String getFields() {
		return this.fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}

}
