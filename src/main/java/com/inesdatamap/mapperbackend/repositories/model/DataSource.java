package com.inesdatamap.mapperbackend.repositories.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * DataSource db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "data_source")
public class DataSource implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4183830263918699985L;

	/**
	 * The identifier of the data source.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The name of the data source.
	 */
	private String name;

	/**
	 * The type of the data source.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DataSourceType type;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public DataSourceType getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DataSourceType type) {
		this.type = type;
	}

}
