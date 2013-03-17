package com.tsystems.javaschool.webmailsystem.dto;

/**
 *
 */
public class FolderDTO {
	private long id;
	private String folderName;

	public FolderDTO(long id, String folderName) {
		this.id = id;
		this.folderName = folderName;
	}

	public FolderDTO(String folderName) {
		this.folderName = folderName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String toString() {
		return folderName;
	}
}
