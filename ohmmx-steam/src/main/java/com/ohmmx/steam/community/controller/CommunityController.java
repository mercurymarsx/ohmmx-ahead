package com.ohmmx.steam.community.controller;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohmmx.common.dto.Result;
import com.ohmmx.steam.community.service.CommunityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/steam/community")
@Tag(name = "Steam社区接口", description = "CommunityController|Steam社区接口")
public class CommunityController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CommunityService communityService;

	@Operation(summary = "刷新拥有的游戏和DLC信息", description = "刷新拥有的游戏和DLC信息.包含名称和运行时间")
	@Parameters({ @Parameter(name = "includeFree", description = "是否包含免费游戏", //
			required = true, in = ParameterIn.QUERY, schema = @Schema(type = "boolean")) })
	@RequestMapping(value = "freshOwnedGames", method = RequestMethod.GET)
	public Result freshOwnedGames(@RequestParam boolean includeFree) {
		try {
			return communityService.freshOwnedGames(includeFree);
		} catch (IOException e) {
			logger.error("freshOwnedGames error", e);
			return new Result(false, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Operation(summary = "刷新徽章", description = "刷新已收集徽章的徽章等级和收集时间")
	@RequestMapping(value = "reshBadgeTime", method = RequestMethod.GET)
	public Result reshBadgeTime() {
		try {
			return communityService.reshBadgeTime();
		} catch (IOException e) {
			logger.error("reshBadgeTime error", e);
			return new Result(false, ExceptionUtils.getRootCauseMessage(e));
		}
	}
}
