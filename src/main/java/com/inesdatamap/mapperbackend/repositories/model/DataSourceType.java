package com.inesdatamap.mapperbackend.repositories.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Data Source Type db entity representation
 *
 * @author gmv
 *
 */
@Entity
public class DataSourceType implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4346836721892536757L;

	/**
	 * The identifier of the data source.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The type of the data source.
	 */

	private String type;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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
