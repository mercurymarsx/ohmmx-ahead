package com.ohmmx.common.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ohmmx.common.entity.column.SteamAppTypeEnum;
import com.ohmmx.common.entity.column.SteamBuyTypeEnum;
import com.ohmmx.common.entity.column.SteamStageEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "STEAM_APP")
public class SteamApp {
	@Id
	private int appid;
	@Enumerated(EnumType.STRING)
	private SteamAppTypeEnum type;
	@Size(max = 255)
	private String name;
	private boolean free;
	private int oriPrice;
	private int buyPrice;
	@Enumerated(EnumType.STRING)
	private SteamBuyTypeEnum buyType;
	@Size(max = 64)
	private String bundleId;
	@Size(max = 64)
	private String category;
	@Enumerated(EnumType.STRING)
	private SteamStageEnum stage;
	private boolean card;
	private boolean finished;
	private boolean banned;
	private int achievements;
	private int doneAchieve;
	private Date releaseDate;
	private Date buyDate;
	private int playedMinites;
	private Date badgeTime;
	private int badgeLevel;
	private int background;
	private int emotion;
	private String description;

	public SteamApp() {
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public SteamAppTypeEnum getType() {
		return type;
	}

	public void setType(SteamAppTypeEnum type) {
		this.type = type;
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

	public int getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(int oriPrice) {
		this.oriPrice = oriPrice;
	}

	public int getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}

	public SteamBuyTypeEnum getBuyType() {
		return buyType;
	}

	public void setBuyType(SteamBuyTypeEnum buyType) {
		this.buyType = buyType;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public SteamStageEnum getStage() {
		return stage;
	}

	public void setStage(SteamStageEnum stage) {
		this.stage = stage;
	}

	public boolean isCard() {
		return card;
	}

	public void setCard(boolean card) {
		this.card = card;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public int getAchievements() {
		return achievements;
	}

	public void setAchievements(int achievements) {
		this.achievements = achievements;
	}

	public int getDoneAchieve() {
		return doneAchieve;
	}

	public void setDoneAchieve(int doneAchieve) {
		this.doneAchieve = doneAchieve;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public int getPlayedMinites() {
		return playedMinites;
	}

	public void setPlayedMinites(int playedMinites) {
		this.playedMinites = playedMinites;
	}

	public Date getBadgeTime() {
		return badgeTime;
	}

	public void setBadgeTime(Date badgeTime) {
		this.badgeTime = badgeTime;
	}

	public int getBadgeLevel() {
		return badgeLevel;
	}

	public void setBadgeLevel(int badgeLevel) {
		this.badgeLevel = badgeLevel;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public int getEmotion() {
		return emotion;
	}

	public void setEmotion(int emotion) {
		this.emotion = emotion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
