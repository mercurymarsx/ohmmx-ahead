package com.ohmmx.steam.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohmmx.common.entity.column.SteamAppTypeEnum;
import com.ohmmx.common.entity.column.SteamStageEnum;
import com.ohmmx.common.utils.DateUtils;
import com.ohmmx.steam.config.Configuration;
import com.ohmmx.steam.shop.dto.AppdetailDto;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SteamAppUtils {
	private static final Logger logger = LoggerFactory.getLogger(SteamAppUtils.class);

	public static AppdetailDto appdetailHandler(String appid, Configuration configuration) throws IOException {
		/** https://wiki.teamfortress.com/wiki/User:RJackson/StorefrontAPI#appdetails */
		HttpUrl.Builder urlBuilder = HttpUrl.parse("https://store.steampowered.com/api/appdetails/").newBuilder();
		urlBuilder.addQueryParameter("l", "chinese");
		urlBuilder.addQueryParameter("cc", "cn");
		urlBuilder.addQueryParameter("appids", appid);

		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		// Proxy
		configuration.requestWithProxy(clientBuilder);

		OkHttpClient client = clientBuilder.build();
		Request request = new Request.Builder().get().url(urlBuilder.build()).build();
		Response response = client.newCall(request).execute();

		AppdetailDto dto = new AppdetailDto();

		if (response.isSuccessful()) {
			String json = response.body().string();

			json = StringUtils.trimToEmpty(json);
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();
			JsonObject appdetail = root.get(String.valueOf(appid)).getAsJsonObject();
			boolean success = appdetail.get("success").getAsBoolean();
			if (!success) {
				dto = null;
				logger.warn(String.format("查询APP\tID:[%s]失败", appid));
			} else {
				JsonObject data = appdetail.get("data").getAsJsonObject();

				// type
				String type = data.get("type").getAsString();
				dto.setTypeString(type.toUpperCase());
				SteamAppTypeEnum typeEnum = SteamAppTypeEnum.valueOf(type.toUpperCase());
				switch (typeEnum) {
				case GAME:
					dto.setType(SteamAppTypeEnum.GAME);
					break;
				case CONFIG:
					dto.setType(SteamAppTypeEnum.CONFIG);
					break;
				case APPLICATION:
					dto.setType(SteamAppTypeEnum.APPLICATION);
					break;
				case MUSIC:
					dto.setType(SteamAppTypeEnum.MUSIC);
					break;
				case VIDEO:
					dto.setType(SteamAppTypeEnum.VIDEO);
					break;
				case DLC:
					dto.setType(SteamAppTypeEnum.DLC);
				default:
					break;
				}
				// free
				boolean free = data.get("is_free").getAsBoolean();
				dto.setFree(free);
				// price
				JsonElement priceOuter = data.get("price_overview");
				int priceInitial = 0;
				if (priceOuter != null) {
					JsonObject price = priceOuter.getAsJsonObject();
					JsonElement priceElement = price.get("initial");
					priceInitial = priceElement == null ? 0 : priceElement.getAsInt();
				}
				dto.setOriPrice(priceInitial);
				// achievements
				JsonElement achievementsOuter = data.get("achievements");
				int achieveTotal = 0;
				if (achievementsOuter != null) {
					JsonObject achievements = achievementsOuter.getAsJsonObject();
					JsonElement achievementsElement = achievements.get("total");
					achieveTotal = achievementsElement == null ? 0 : achievementsElement.getAsInt();
				}
				dto.setAchievements(achieveTotal);
				// release_date
				JsonObject release = data.get("release_date").getAsJsonObject();
				boolean coming = release.get("coming_soon").getAsBoolean();
				if (coming) {
					dto.setCategory("81"); // 81.尚未发行
				} else {
					String releaseDate = release.get("date").getAsString();
					dto.setReleaseDate(parseDate(releaseDate));
				}
				// name
				String name = data.get("name").getAsString();
				dto.setName(name);
				// other
				if (dto.getStage() == null) {
					dto.setStage(SteamStageEnum.TOBUY);
				}
				// parent
				JsonElement fullgameElement = data.get("fullgame");
				if (fullgameElement != null) {
					JsonObject fullgame = fullgameElement.getAsJsonObject();
					int parentId = fullgame.get("appid").getAsInt();
					dto.setParentId(parentId);
				}

				// dlc
				List<Integer> dlc = new ArrayList<Integer>();
				JsonElement dlcElement = data.get("dlc");
				if (dlcElement != null) {
					JsonArray dlcList = dlcElement.getAsJsonArray();
					if (dlcList != null) {
						dlcList.forEach(d -> {
							dlc.add(d.getAsInt());
						});
					}
				}
				dto.setDlc(dlc);
			}
		} else {
			dto = null;
			logger.warn(String.format("查询失败,响应STATUS[%s]", response.code()));
		}
		return dto;
	}

	private static Date parseDate(String sdate) {
		Date date = DateUtils.yyyy_MM_dd("1970-01-01");
		try {
			date = DateTimeFormat.forPattern("dd MMM, yyyy").withLocale(Locale.US).parseDateTime(sdate).toDate();
		} catch (Exception e1) {
			logger.warn(String.format("日期[%s]不是美国时间格式,将使用中国时间格式重试.", sdate));
			try {
				date = DateUtils.parse("yyyy年MM月dd日", sdate);
			} catch (Exception e2) {
				logger.warn(String.format("日期[%s]不是美国时间格式,也不是中国时间格式.", sdate));
			}
		}
		return date;
	}
}
