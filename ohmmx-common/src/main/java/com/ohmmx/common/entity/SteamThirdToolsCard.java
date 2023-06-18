package com.ohmmx.common.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "STEAM_THIRD_TOOLS_CARD")
public class SteamThirdToolsCard {
	@Size(max = 255)
	private String game;
	private Integer owned;
	private Integer uniqee;
	private Integer cards;
	private Integer badgeLvl;
	@Size(max = 255)
	private String setPrice;
	@Size(max = 255)
	private String priceDiff;
	@Size(max = 255)
	private String cardAvg;
	@Size(max = 255)
	private String boosterAvg;
	@Size(max = 255)
	private String boosterPercent;
	@Size(max = 255)
	private String emoteAvg;
	@Size(max = 255)
	private String bgAvg;
	@Size(max = 255)
	private String avgQty;
	@Size(max = 255)
	private String discount;
	private Date added;
	@Id
	private int appid;

	public SteamThirdToolsCard() {
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Integer getOwned() {
		return owned;
	}

	public void setOwned(Integer owned) {
		this.owned = owned;
	}

	public Integer getUniqee() {
		return uniqee;
	}

	public void setUniqee(Integer uniqee) {
		this.uniqee = uniqee;
	}

	public Integer getCards() {
		return cards;
	}

	public void setCards(Integer cards) {
		this.cards = cards;
	}

	public Integer getBadgeLvl() {
		return badgeLvl;
	}

	public void setBadgeLvl(Integer badgeLvl) {
		this.badgeLvl = badgeLvl;
	}

	public String getSetPrice() {
		return setPrice;
	}

	public void setSetPrice(String setPrice) {
		this.setPrice = setPrice;
	}

	public String getPriceDiff() {
		return priceDiff;
	}

	public void setPriceDiff(String priceDiff) {
		this.priceDiff = priceDiff;
	}

	public String getCardAvg() {
		return cardAvg;
	}

	public void setCardAvg(String cardAvg) {
		this.cardAvg = cardAvg;
	}

	public String getBoosterAvg() {
		return boosterAvg;
	}

	public void setBoosterAvg(String boosterAvg) {
		this.boosterAvg = boosterAvg;
	}

	public String getBoosterPercent() {
		return boosterPercent;
	}

	public void setBoosterPercent(String boosterPercent) {
		this.boosterPercent = boosterPercent;
	}

	public String getEmoteAvg() {
		return emoteAvg;
	}

	public void setEmoteAvg(String emoteAvg) {
		this.emoteAvg = emoteAvg;
	}

	public String getBgAvg() {
		return bgAvg;
	}

	public void setBgAvg(String bgAvg) {
		this.bgAvg = bgAvg;
	}

	public String getAvgQty() {
		return avgQty;
	}

	public void setAvgQty(String avgQty) {
		this.avgQty = avgQty;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Date getAdded() {
		return added;
	}

	public void setAdded(Date added) {
		this.added = added;
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
