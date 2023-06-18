package com.ohmmx.common.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ohmmx.common.entity.column.ImageTypeEnum;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "WALLPAPER_TASK")
public class WallpaperTask {
	@EmbeddedId
	private ID id;
	@NotNull
	private String month;
	@NotNull
	private String reason;

	public WallpaperTask() {
	}

	public WallpaperTask(ID id, String month, String reason) {
		this.id = id;
		this.month = month;
		this.reason = reason;
	}

	@Embeddable
	public static class ID implements Serializable {
		private static final long serialVersionUID = 1L;

		@NotNull
		private String filenum;
		@NotNull
		@Enumerated(EnumType.STRING)
		private ImageTypeEnum type;

		public ID() {
		}

		public ID(String filenum, ImageTypeEnum type) {
			this.filenum = filenum;
			this.type = type;
		}

		public String getFilenum() {
			return filenum;
		}

		public void setFilenum(String filenum) {
			this.filenum = filenum;
		}

		public ImageTypeEnum getType() {
			return type;
		}

		public void setType(ImageTypeEnum type) {
			this.type = type;
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
