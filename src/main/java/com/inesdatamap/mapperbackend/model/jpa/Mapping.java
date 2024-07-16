package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Mapping db entity representation
 *
 * @author gmv
 *
 */
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
	 * Rules associated with the mapping.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_id")
	private List<MappingRule> mappingRules;

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
	private List<Namespace> namespaces;

	/**
	 * The path in MinIO storage.
	 */
	@Column(name = "minio_path")
	private String minioPath;

	/**
	 * Data Virtualization Catalog (DVC) revision for the mapping.
	 */
	@Column(name = "dvc_revision")
	private String dvcRevision;

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
	 * @return the mappingRules
	 */
	public List<MappingRule> getMappingRules() {
		return this.mappingRules;
	}

	/**
	 * @param mappingRules
	 *            the mappingRules to set
	 */
	public void setMappingRules(List<MappingRule> mappingRules) {
		this.mappingRules = mappingRules;
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
		return this.namespaces;
	}

	/**
	 * @param namespaces
	 *            the namespaces to set
	 */
	public void setNamespaces(List<Namespace> namespaces) {
		this.namespaces = namespaces;
	}

	/**
	 * @return the minioPath
	 */
	public String getMinioPath() {
		return this.minioPath;
	}

	/**
	 * @param minioPath
	 *            the minioPath to set
	 */
	public void setMinioPath(String minioPath) {
		this.minioPath = minioPath;
	}

	/**
	 * @return the dvcRevision
	 */
	public String getDvcRevision() {
		return this.dvcRevision;
	}

	/**
	 * @param dvcRevision
	 *            the dvcRevision to set
	 */
	public void setDvcRevision(String dvcRevision) {
		this.dvcRevision = dvcRevision;
	}

}
