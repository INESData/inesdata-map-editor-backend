package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Ontology db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@Entity
@Table(name = "ONTOLOGY")
public class Ontology extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8392849567782624864L;

	/**
	 * The name of the ontology.
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * The content of the ontology.
	 */
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;

	/**
	 * The title of the ontology.
	 */
	@Column(name = "title")
	private String title;

	/**
	 * The date when the ontology was uploaded.
	 */
	@Column(name = "upload_date")
	private LocalDateTime uploadDate;

	/**
	 * The URL associated with the ontology.
	 */
	@Column(name = "url")
	private String url;

	/**
	 * The version number of the ontology.
	 */
	@Column(name = "version_name")
	private String versionName;

}
