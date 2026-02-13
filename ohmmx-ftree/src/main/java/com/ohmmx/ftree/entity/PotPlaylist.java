package com.ohmmx.ftree.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "POT_PLAYLIST")
public class PotPlaylist {

	private String filePath;
	private String folderName;
	private String fileName;
	private Long duration;

	@Id
	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	@Column(name = "FOLDER_NAME")
	public String getFolderName() {
		return folderName;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	@Column(name = "DURATION")
	public Long getDuration() {
		return duration;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
}
