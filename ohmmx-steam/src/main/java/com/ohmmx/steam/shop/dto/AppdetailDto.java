package com.ohmmx.steam.shop.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ohmmx.common.entity.column.SteamAppTypeEnum;
import com.ohmmx.common.entity.column.SteamStageEnum;

public class AppdetailDto {
	private String name;
	private boolean free;
	private SteamAppTypeEnum type;
	private String typeString;
	private SteamStageEnum stage;
	private int oriPrice;
	private int achievements;
	private String category;
	private Date releaseDate;
	private int parentId;
	private List<Integer> dlc;

	public AppdetailDto() {
		this.dlc = new ArrayList<Integer>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public SteamAppTypeEnum getType() {
		return type;
	}

	public void setType(SteamAppTypeEnum type) {
		this.type = type;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public SteamStageEnum getStage() {
		return stage;
	}

	public void setStage(SteamStageEnum stage) {
		this.stage = stage;
	}

	public int getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(int oriPrice) {
		this.oriPrice = oriPrice;
	}

	public int getAchievements() {
		return achievements;
	}

	public void setAchievements(int achievements) {
		this.achievements = achievements;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public List<Integer> getDlc() {
		return dlc;
	}

	public void setDlc(List<Integer> dlc) {
		this.dlc = dlc;
	}
}
