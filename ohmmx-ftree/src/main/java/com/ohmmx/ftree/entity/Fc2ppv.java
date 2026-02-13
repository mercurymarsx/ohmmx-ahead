package com.ohmmx.ftree.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FC2PPV")
public class Fc2ppv {

	private long id;
	private String monthly;
	private String fc2name;
	private String videoName;
	private String status;

	@Id
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	@Column(name = "MONTHLY")
	public String getMonthly() {
		return monthly;
	}

	@Column(name = "FC2NAME")
	public String getFc2name() {
		return fc2name;
	}

	@Column(name = "VD_NAME")
	public String getVideoName() {
		return videoName;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}

	public void setFc2name(String fc2name) {
		this.fc2name = fc2name;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
