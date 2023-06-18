package com.ohmmx.common.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ohmmx.common.entity.column.SteamWishEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "STEAM_WISHLIST")
public class SteamWishlist {
	@Id
	private int appid;
	@Enumerated(EnumType.STRING)
	private SteamWishEnum type;
	private int salePrice;
	private int currentPrice;
	private int lowestPrice;
	private int offRate;
	private Date saleDate;
	@Size(max = 255)
	private String name;
	@Size(max = 255)
	private String transName;
	@Size(max = 255)
	private String link;

	public SteamWishlist() {
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public SteamWishEnum getType() {
		return type;
	}

	public void setType(SteamWishEnum type) {
		this.type = type;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(int currentPrice) {
		this.currentPrice = currentPrice;
	}

	public int getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(int lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public int getOffRate() {
		return offRate;
	}

	public void setOffRate(int offRate) {
		this.offRate = offRate;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
