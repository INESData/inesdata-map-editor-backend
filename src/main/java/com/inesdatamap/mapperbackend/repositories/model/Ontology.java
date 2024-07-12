package com.inesdatamap.mapperbackend.repositories.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

/**
 * Ontology db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "ontology")
public class Ontology implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8392849567782624864L;

	/**
	 * The identifier of the data source.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The name of the ontology.
	 */
	@NotEmpty
	private String name;

	/**
	 * The content of the ontology.
	 */
	private String content;

	/**
	 * The title of the ontology.
	 */
	private String title;

	/**
	 * The date when the ontology was uploaded.
	 */
	private LocalDateTime uploadDate;

	/**
	 * The user who uploaded the ontology.
	 */
	private String user;

	/**
	 * The URL associated with the ontology.
	 */
	private String url;

	/**
	 * The version number of the ontology.
	 */
	private int versionNum;

	/**
	 * @return the id
	 */

	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	void setId(Long id) {
		this.id = id;
	}

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
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
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
	 * @return the versionNum
	 */
	public int getVersionNum() {
		return this.versionNum;
	}

	/**
	 * @param versionNum
	 *            the versionNum to set
	 */
	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

}
