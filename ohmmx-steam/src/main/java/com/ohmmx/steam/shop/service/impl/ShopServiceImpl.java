package com.ohmmx.steam.shop.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohmmx.common.dto.Result;
import com.ohmmx.common.entity.SteamApp;
import com.ohmmx.common.entity.SteamDlc;
import com.ohmmx.common.entity.SteamHandlerNew;
import com.ohmmx.common.mapper.SteamAppRepository;
import com.ohmmx.common.mapper.SteamDlcRepository;
import com.ohmmx.common.mapper.SteamHandlerNewRepository;
import com.ohmmx.steam.config.Configuration;
import com.ohmmx.steam.shop.dto.AppdetailDto;
import com.ohmmx.steam.shop.dto.StoreAppDto;
import com.ohmmx.steam.shop.service.ShopService;
import com.ohmmx.steam.utils.SteamAppUtils;

import jakarta.transaction.Transactional;

@Service
public class ShopServiceImpl implements ShopService {
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
	public Result addmanual(StoreAppDto dto) {
		Result result = new Result();

		dto.getApps().forEach(appid -> {
			SteamApp app = steamAppRepository.findById(appid).orElse(null);
			if (app == null) {
				app = new SteamApp();
				app.setAppid(appid);
				steamAppRepository.save(app);
			}
		});

		dto.getDlcs().forEach(dlcid -> {
			SteamDlc dlc = steamDlcRepository.findById(dlcid).orElse(null);
			if (dlc == null) {
				dlc = new SteamDlc();
				dlc.setAppid(dlcid);
				steamDlcRepository.save(dlc);
			}
		});

		result.setSuccess(true);
		result.setMessage("成功");
		return result;
	}

	@Override
	@Transactional
	public Result fetchAppdetails(boolean containDlc) {
		Result result = new Result();

		// APP
		List<SteamApp> appList = steamAppRepository.findEmpty();
		appList.forEach(app -> {
			int appid = app.getAppid();
			logger.info("查询APP\tID:[" + appid + "]");

			AppdetailDto appDto;
			try {
				appDto = SteamAppUtils.appdetailHandler(String.valueOf(appid), configuration);
			} catch (IOException e) {
				logger.error("appdetailHandler error", e);
				return;
			}
			if (appDto != null) {
				app.setName(appDto.getName());
				app.setFree(appDto.isFree());
				app.setType(appDto.getType());
				app.setStage(appDto.getStage());
				app.setOriPrice(appDto.getOriPrice());
				app.setAchievements(appDto.getAchievements());
				app.setCategory(appDto.getCategory());
				app.setReleaseDate(appDto.getReleaseDate());

				steamAppRepository.save(app);

				if (containDlc) {
					List<Integer> dlcList = appDto.getDlc();
					dlcList.forEach(dlcid -> {
						AppdetailDto dlcDto;
						try {
							dlcDto = SteamAppUtils.appdetailHandler(String.valueOf(dlcid), configuration);
						} catch (IOException e) {
							logger.error("appdetailHandler error", e);
							return;
						}
						if (dlcDto != null) {
							SteamDlc dlc = steamDlcRepository.findById(dlcid).orElse(null);
							if (dlc == null) {
								dlc = new SteamDlc();
								dlc.setAppid(dlcid);
								dlc.setParentId(appid);
							}
							logger.info(String.format("发现DLC\tAPPID:[%s]===>DLC[%s]", appid, dlcid));
							dlc.setName(dlcDto.getName());
							dlc.setFree(dlcDto.isFree());
							dlc.setOriPrice(dlcDto.getOriPrice());
							dlc.setReleaseDate(dlcDto.getReleaseDate());
							steamDlcRepository.save(dlc);
						}
					});
				}
			} else {
				return;
			}
		});

		result.setSuccess(true);
		result.setMessage("成功");
		return result;
	}

	@Override
	@Transactional
	public Result fetchDlcdetails() {
		Result result = new Result();

		// DLC
		List<SteamDlc> dlcList = steamDlcRepository.findEmpty();
		dlcList.forEach(dlc -> {
			int dlcid = dlc.getAppid();
			logger.info("查询DLC\tID:[" + dlcid + "]");

			AppdetailDto dlcDto;
			try {
				dlcDto = SteamAppUtils.appdetailHandler(String.valueOf(dlcid), configuration);
			} catch (IOException e) {
				logger.error("appdetailHandler error", e);
				return;
			}
			if (dlcDto != null) {
				dlc.setName(dlcDto.getName());
				dlc.setFree(dlcDto.isFree());
				dlc.setOriPrice(dlcDto.getOriPrice());
				dlc.setReleaseDate(dlcDto.getReleaseDate());
				dlc.setParentId(dlcDto.getParentId());
				steamDlcRepository.save(dlc);
			} else {
				return;
			}
		});

		result.setSuccess(true);
		result.setMessage("成功");
		return result;
	}

	@Override
	@Transactional
	public Result analyzenew() {
		Result result = new Result();

		List<SteamHandlerNew> newappList = steamHandlerNewRepository.findAsc10();
		newappList.forEach(newapp -> {
			AppdetailDto appDto;
			try {
				appDto = SteamAppUtils.appdetailHandler(String.valueOf(newapp.getAppid()), configuration);
			} catch (IOException e) {
				logger.error("appdetailHandler error", e);
				return;
			}
			if (appDto == null) {
				return;
			}
			if (appDto.getType() == null) {
				newapp.setAnalysed(true);
				newapp.setType(appDto.getTypeString());
				steamHandlerNewRepository.save(newapp);
				return;
			}
			switch (appDto.getType()) {
			case DLC:
				newapp.setType("DLC");
				SteamDlc dlc = new SteamDlc();
				dlc.setAppid(newapp.getAppid());
				dlc.setName(appDto.getName());
				dlc.setFree(appDto.isFree());
				dlc.setOriPrice(appDto.getOriPrice());
				dlc.setReleaseDate(appDto.getReleaseDate());
				dlc.setParentId(appDto.getParentId());
				steamDlcRepository.save(dlc);
				break;
			default:
				newapp.setType("APP");
				SteamApp app = new SteamApp();
				app.setAppid(newapp.getAppid());
				app.setName(appDto.getName());
				app.setFree(appDto.isFree());
				app.setType(appDto.getType());
				app.setStage(appDto.getStage());
				app.setOriPrice(appDto.getOriPrice());
				app.setAchievements(appDto.getAchievements());
				app.setCategory(appDto.getCategory());
				app.setReleaseDate(appDto.getReleaseDate());
				steamAppRepository.save(app);
				break;
			}
			newapp.setAnalysed(true);
			steamHandlerNewRepository.save(newapp);
		});

		logger.info("解析新增APP完成");
		result.setSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
