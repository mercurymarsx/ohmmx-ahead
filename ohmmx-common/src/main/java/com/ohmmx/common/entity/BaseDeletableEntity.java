package com.ohmmx.common.entity;

import java.util.Date;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseDeletableEntity extends BaseTimestampEntity {
	protected Date deleteTimestamp;
	protected boolean deleteFlag;

	public Date getDeleteTimestamp() {
		return deleteTimestamp;
	}

	public void setDeleteTimestamp(Date deleteTimestamp) {
		this.deleteTimestamp = deleteTimestamp;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
