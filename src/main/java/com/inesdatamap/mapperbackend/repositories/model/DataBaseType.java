package com.inesdatamap.mapperbackend.repositories.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Data Base Type db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "data_base_type")
public class DataBaseType implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3573243002457605146L;

	/**
	 * The identifier of the data base.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * The type of the data base.
	 */
	@Column(name = "type")
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
