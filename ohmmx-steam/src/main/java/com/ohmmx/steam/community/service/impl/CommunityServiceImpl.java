package com.ohmmx.steam.community.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohmmx.common.dto.Result;
import com.ohmmx.common.entity.SteamApp;
import com.ohmmx.common.entity.SteamDlc;
import com.ohmmx.common.entity.SteamHandlerNew;
import com.ohmmx.common.mapper.SteamAppRepository;
import com.ohmmx.common.mapper.SteamDlcRepository;
import com.ohmmx.common.mapper.SteamHandlerNewRepository;
import com.ohmmx.steam.community.service.CommunityService;
import com.ohmmx.steam.config.Configuration;

import jakarta.transaction.Transactional;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class CommunityServiceImpl implements CommunityService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Configuration configuration;

	@Autowired
	private SteamAppRepository steamAppRepository;
	@Autowired
	private SteamDlcRepository steamDlcRepository;
	@Autowired
	private SteamHandlerNewRepository steamHandlerNewRepository;

	@Override
	@Transactional
	public Result freshOwnedGames(boolean includeFree) throws IOException {
		Result result = new Result();

		/** https://partner.steamgames.com/doc/webapi/IPlayerService#GetOwnedGames */
		HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/").newBuilder();
		urlBuilder.addQueryParameter("l", "chinese");
		urlBuilder.addQueryParameter("cc", "cn");
		urlBuilder.addQueryParameter("key", configuration.getSteamConfig().getApikey());
		urlBuilder.addQueryParameter("steamid", configuration.getSteamConfig().getId64());
		urlBuilder.addQueryParameter("include_appinfo", "1");
		urlBuilder.addQueryParameter("include_played_free_games", includeFree ? "1" : "0");

		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		// Proxy
		configuration.requestWithProxy(clientBuilder);

		OkHttpClient client = clientBuilder.build();
		Request request = new Request.Builder().get().url(urlBuilder.build()).build();
		Response response = client.newCall(request).execute();

		if (response.isSuccessful()) {
			String json = StringUtils.trimToEmpty(response.body().string());
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();
			JsonObject base = root.get("response").getAsJsonObject();
			// int gameCount = base.get("game_count").getAsInt();
			JsonArray apps = base.get("games").getAsJsonArray();

			steamHandlerNewRepository.deleteAll();
			List<SteamApp> saveApp = new ArrayList<SteamApp>();
			List<SteamDlc> saveDlc = new ArrayList<SteamDlc>();
			List<SteamHandlerNew> saveNew = new ArrayList<SteamHandlerNew>();
			apps.forEach(appElement -> {
				JsonObject appObject = appElement.getAsJsonObject();
				int appid = appObject.get("appid").getAsInt();
				String name = appObject.get("name").getAsString();
				int playtime = appObject.get("playtime_forever").getAsInt();

				logger.debug(String.format("开始刷新APP[%s]信息", appid));
				boolean newapp = false;
				SteamApp app = steamAppRepository.findById(appid).orElse(null);
				if (app != null) {
					newapp = false;

					app.setName(name);
					if (playtime > 0) {
						app.setPlayedMinites(playtime);
					}
					saveApp.add(app);
					// steamAppRepository.save(app);
					return;
				} else {
					newapp = true;
				}

				SteamDlc dlc = steamDlcRepository.findById(appid).orElse(null);
				if (dlc != null) {
					newapp = false;

					dlc.setName(name);
					saveDlc.add(dlc);
					// steamDlcRepository.save(dlc);
					return;
				} else {
					newapp = true;
				}

				if (newapp) {
					// 保存到过渡表,后续加入APP或者DLC.如果连续请求会返回429请求过多.
					SteamHandlerNew newObject = steamHandlerNewRepository.findById(appid).orElse(null);
					if (newObject == null) {
						newObject = new SteamHandlerNew();
						newObject.setAppid(appid);
						newObject.setAnalysed(false);
						saveNew.add(newObject);
					}
					// steamHandlerNewRepository.save(newObject);
				}
			});
			steamAppRepository.saveAll(saveApp);
			steamDlcRepository.saveAll(saveDlc);
			steamHandlerNewRepository.saveAll(saveNew);

			logger.info(String.format("刷新APP信息完成.总数[%s]其中刷新APP数[%s]DLC数[%s]新增数[%s].", //
					apps.size(), saveApp.size(), saveDlc.size(), saveNew.size()));
		} else {
			String message = String.format("查询失败,响应STATUS[%s]", response.code());
			logger.warn(message);
			result.setSuccess(false);
			result.setMessage(message);
			return result;
		}

		result.setSuccess(true);
		result.setMessage("成功");
		return result;
	}

	@Override
	@Transactional
	public Result reshBadgeTime() throws IOException {
		Result result = new Result();

		HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.steampowered.com/IPlayerService/GetBadges/v1/").newBuilder();
		urlBuilder.addQueryParameter("l", "chinese");
		urlBuilder.addQueryParameter("cc", "cn");
		urlBuilder.addQueryParameter("key", configuration.getSteamConfig().getApikey());
		urlBuilder.addQueryParameter("steamid", configuration.getSteamConfig().getId64());

		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		// Proxy
		configuration.requestWithProxy(clientBuilder);

		OkHttpClient client = clientBuilder.build();
		Request request = new Request.Builder().get().url(urlBuilder.build()).build();
		Response response = client.newCall(request).execute();

		if (response.isSuccessful()) {
			String json = StringUtils.trimToEmpty(response.body().string());
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();
			JsonObject base = root.get("response").getAsJsonObject();

			JsonArray badges = base.get("badges").getAsJsonArray();
			badges.forEach(badge -> {
				JsonObject badgeObj = badge.getAsJsonObject();
				JsonElement appidEle = badgeObj.get("appid");
				if (appidEle != null) {
					int appid = appidEle.getAsInt();
					int badgeLevel = badgeObj.get("level").getAsInt();
					long badgeTime = badgeObj.get("completion_time").getAsLong();
					Date badgeDate = new Date(badgeTime * 1000);

					SteamApp app = steamAppRepository.findById(appid).orElse(null);
					if (app != null) {
						app.setBadgeLevel(badgeLevel);
						app.setBadgeTime(badgeDate);
						steamAppRepository.save(app);
					}
				}
			});
		}

		result.setSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
