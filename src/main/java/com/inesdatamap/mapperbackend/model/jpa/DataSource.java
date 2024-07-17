package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DataSource db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@ToString
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
	@Column(name = "type")
	private DataSourceTypeEnum type;
}
