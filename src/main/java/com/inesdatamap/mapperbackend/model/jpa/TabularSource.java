package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tabular Source db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@ToString
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
