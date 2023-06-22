package com.ohmmx.steam.community.service;

import java.io.IOException;

import com.ohmmx.common.dto.Result;

public interface CommunityService {
	/**
	 * 刷新拥有的游戏和DLC信息.包含名称和运行时间
	 *
	 * @param includeFree 是否包含免费游戏
	 * @return 结果
	 */
	Result freshOwnedGames(boolean includeFree) throws IOException;

	/**
	 * 刷新已收集徽章的徽章等级和收集时间
	 *
	 * @return 结果
	 */
	Result reshBadgeTime() throws IOException;
}
