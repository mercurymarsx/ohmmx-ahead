package com.ohmmx.common.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import com.ohmmx.common.entity.column.SteamTradeFlagEnum;
import com.ohmmx.common.entity.column.SteamTradeTypeEnum;
import com.ohmmx.common.id.IdGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "STEAM_TRADE")
public class SteamTrade {

	@Id
	@GenericGenerator(name = "idGen", type = IdGenerator.class)
	@GeneratedValue(generator = "idGen")
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NotNull
	@Size(max = 16)
	private String appid;
	@Enumerated(EnumType.STRING)
	private SteamTradeFlagEnum tradeFlag;
	@Enumerated(EnumType.STRING)
	private SteamTradeTypeEnum tradeType;
	@Size(max = 255)
	private String tradeName;
	private Date onshelfTime;
	@Max(127)
	private int quantity;

	public SteamTrade() {
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public SteamTradeFlagEnum getTradeFlag() {
		return tradeFlag;
	}

	public void setTradeFlag(SteamTradeFlagEnum tradeFlag) {
		this.tradeFlag = tradeFlag;
	}

	public SteamTradeTypeEnum getTradeType() {
		return tradeType;
	}

	public void setTradeType(SteamTradeTypeEnum tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public Date getOnshelfTime() {
		return onshelfTime;
	}

	public void setOnshelfTime(Date onshelfTime) {
		this.onshelfTime = onshelfTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
