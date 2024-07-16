package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * DataSource db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "DATA_SOURCE")
public class DataSource extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4183830263918699985L;

	/**
	 * The name of the data source.
	 */
	@Column(name = "name")
	private String name;

	/**
	 * The type of the data source.
	 */
	@JoinColumn(name = "type")
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
