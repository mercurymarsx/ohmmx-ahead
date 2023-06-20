package com.ohmmx.common.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamDlc;

@Repository
public interface SteamDlcRepository extends JpaRepository<SteamDlc, Integer> {
	@Query("FROM SteamDlc WHERE buyDate IS NULL")
	List<SteamDlc> findEmpty();
}
