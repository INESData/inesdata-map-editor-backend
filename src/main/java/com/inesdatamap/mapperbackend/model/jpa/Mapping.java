package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Mapping db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
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
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * Fields associated with the mapping.
	 */
	@OneToMany(mappedBy = "mapping", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MappingField> fields = new ArrayList<>();

	/**
	 * Resource Mapping Language (RML) associated with the mapping.
	 */
	@Lob
	@Column(name = "rml")
	@Basic(fetch = FetchType.LAZY)
	private byte[] rml;

	/**
	 * Namespaces associated with the mapping.
	 */
	@OneToMany(mappedBy = "mapping", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Namespace> namespaces = new ArrayList<>();

}
