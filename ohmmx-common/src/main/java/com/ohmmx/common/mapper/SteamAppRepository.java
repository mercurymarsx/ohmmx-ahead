package com.ohmmx.common.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamApp;

@Repository
public interface SteamAppRepository extends JpaRepository<SteamApp, Integer> {
	@Query("FROM SteamApp WHERE buyDate IS NULL")
	List<SteamApp> findEmpty();
}
