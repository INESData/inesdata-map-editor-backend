package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mapping_id")
	private List<MappingField> fields = new ArrayList<>();

	/**
	 * Resource Mapping Language (RML) associated with the mapping.
	 */
	@Lob
	@Column(name = "rml")
	private String rml;

	/**
	 * Namespaces associated with the mapping.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mapping_id")
	private List<Namespace> namespaces = new ArrayList<>();

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
	 * @return the fields
	 */
	public List<MappingField> getFields() {
		return new ArrayList<>(this.fields);
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<MappingField> fields) {
		this.fields = new ArrayList<>(fields);
	}

	/**
	 * @return the rml
	 */
	public String getRml() {
		return this.rml;
	}

	/**
	 * @param rml
	 *            the rml to set
	 */
	public void setRml(String rml) {
		this.rml = rml;
	}

	/**
	 * @return the namespaces
	 */
	public List<Namespace> getNamespaces() {
		return new ArrayList<>(this.namespaces);
	}

	/**
	 * @param namespaces
	 *            the namespaces to set
	 */
	public void setNamespaces(List<Namespace> namespaces) {
		this.namespaces = new ArrayList<>(namespaces);
	}

}
