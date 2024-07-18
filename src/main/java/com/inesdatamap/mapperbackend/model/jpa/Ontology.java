package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ontology db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@ToString
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
	@Column(name = "name")
	private String name;

	/**
	 * The content of the ontology.
	 */
	@Lob
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
	void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the uploadDate
	 */
	public LocalDateTime getUploadDate() {
		return this.uploadDate;
	}

	/**
	 * @param uploadDate
	 *            the uploadDate to set
	 */
	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return this.versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

}
