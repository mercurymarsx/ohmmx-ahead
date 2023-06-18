package com.ohmmx.common.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ohmmx.common.entity.column.SteamBuyTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "STEAM_BUNDLE")
public class SteamBundle {
	@Id
	@Size(max = 64)
	private String bundleid;
	@Size(max = 255)
	private String name;
	@Size(max = 16)
	private String corporation;
	@Size(max = 64)
	private String packId;
	@Enumerated(EnumType.STRING)
	private SteamBuyTypeEnum currency;
	private int price;

	public SteamBundle() {
	}

	public String getBundleid() {
		return bundleid;
	}

	public void setBundleid(String bundleid) {
		this.bundleid = bundleid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCorporation() {
		return corporation;
	}

	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public SteamBuyTypeEnum getCurrency() {
		return currency;
	}

	public void setCurrency(SteamBuyTypeEnum currency) {
		this.currency = currency;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
