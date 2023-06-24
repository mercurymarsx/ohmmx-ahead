package com.ohmmx.steam.shop.service;

import com.ohmmx.common.dto.Result;
import com.ohmmx.steam.shop.dto.StoreAppDto;

public interface ShopService {
	/**
	 * 手动添加应用
	 *
	 * @param dto APP和DLC列表
	 * @return 结果
	 */
	Result addmanual(StoreAppDto dto);

	/**
	 * 获取新增的APP包含DLC信息.
	 *
	 * @param containDlc 是否包含DLC
	 * @return 结果
	 */
	Result fetchAppdetails(boolean containDlc);

	/**
	 * 单独获取新增的DLC信息.
	 *
	 * @return 结果
	 */
	Result fetchDlcdetails();

	/**
	 * 解析新增APP.将SteamHandlerNew里按appid升序解析前10个,一次解析多了会报请求过多.
	 *
	 * @return 结果
	 */
	Result analyzenew();
}
