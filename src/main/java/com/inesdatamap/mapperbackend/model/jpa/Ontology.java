package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
	@NotNull
	@Column(name = "name")
	private String name;

	/**
	 * The content of the ontology.
	 */
	@Column(name = "content")
	private String content;

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
