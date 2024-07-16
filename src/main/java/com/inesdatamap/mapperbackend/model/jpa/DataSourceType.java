package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Data Source Type db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "DATA_SOURCE_TYPE")
public class DataSourceType extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4346836721892536757L;

	/**
	 * The type of the data source.
	 */
	@Column(name = "type")
	private String type;

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
