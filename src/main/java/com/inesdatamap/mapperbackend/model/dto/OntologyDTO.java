package com.inesdatamap.mapperbackend.model.dto;

import java.time.LocalDateTime;

/**
 * DTO representing an Ontology.
 *
 */
public class OntologyDTO extends BaseEntityDTO {

	private String name;

	private String content;

	private String title;

	private LocalDateTime uploadDate;

	private String url;

	private int versionName;

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
	public int getVersionName() {
		return this.versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionName(int versionName) {
		this.versionName = versionName;
	}

}
