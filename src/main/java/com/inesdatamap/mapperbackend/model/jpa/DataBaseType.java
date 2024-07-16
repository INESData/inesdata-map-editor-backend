package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Data Base Type db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "DATA_BASE_TYPE")
public class DataBaseType extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3573243002457605146L;

	/**
	 * The type of the data base.
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
