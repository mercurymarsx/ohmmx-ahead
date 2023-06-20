package com.ohmmx.common.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamHandlerNew;

@Repository
public interface SteamHandlerNewRepository extends JpaRepository<SteamHandlerNew, Integer> {
	@Query("DELETE FROM SteamHandlerNew WHERE type IN ('APP', 'DLC')")
	@Modifying
	void deleteAll();

	@Query(nativeQuery = true, value = "SELECT APPID, TYPE, ANALYSED " //
			+ "FROM STEAM_HANDLER_NEW WHERE ANALYSED = FALSE ORDER BY APPID ASC LIMIT 10")
	List<SteamHandlerNew> findAsc10();
}
