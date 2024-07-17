package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mapping db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "MAPPING")
public class Mapping extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4239287388230556730L;

	/**
	 * The name of the mapping.
	 */
	@Column(name = "name")
	private String name;

	/**
	 * Fields associated with the mapping.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_id")
	private List<MappingField> fields = new ArrayList<>();

	/**
	 * Resource Mapping Language (RML) associated with the mapping.
	 */
	@Column(name = "rml")
	private String rml;

	/**
	 * Namespaces associated with the mapping.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_id")
	private List<Namespace> namespaces = new ArrayList<>();
}
