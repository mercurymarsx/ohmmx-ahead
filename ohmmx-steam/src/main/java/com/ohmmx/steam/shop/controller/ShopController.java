package com.ohmmx.steam.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohmmx.common.dto.Result;
import com.ohmmx.steam.shop.dto.StoreAppDto;
import com.ohmmx.steam.shop.service.ShopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/steam/shop")
@Tag(name = "Steam商城接口", description = "ShopController|Steam商城接口")
public class ShopController {
	@Autowired
	private ShopService shopService;

	@Operation(summary = "添加应用", description = "手动添加APP和DLC.(部分下架的无法通过已拥有获取)")
	@RequestMapping(value = "addmanual", method = RequestMethod.POST)
	public Result addmanual(@Parameter(name = "dto", description = "需要添加的APP和DLC", required = true) @RequestBody StoreAppDto dto) {
		return shopService.addmanual(dto);
	}

	@Operation(summary = "获取APP信息", description = "需要预先通过添加应用存入库")
	@Parameters({ @Parameter(name = "containDlc", description = "是否包含DLC", //
			required = true, in = ParameterIn.QUERY, schema = @Schema(type = "boolean")) })
	@RequestMapping(value = "appdetails", method = RequestMethod.GET)
	public Result appdetails(@RequestParam boolean containDlc) {
		return shopService.fetchAppdetails(containDlc);
	}

	@Operation(summary = "单独获取DLC信息", description = "需要预先通过添加应用存入库")
	@RequestMapping(value = "dlcdetails", method = RequestMethod.GET)
	public Result dlcdetails() {
		return shopService.fetchDlcdetails();
	}

	@Operation(summary = "解析新增APP", description = "将SteamHandlerNew里按appid升序解析前10个")
	@RequestMapping(value = "analyzenew", method = RequestMethod.GET)
	public Result analyzenew() {
		return shopService.analyzenew();
	}
}
