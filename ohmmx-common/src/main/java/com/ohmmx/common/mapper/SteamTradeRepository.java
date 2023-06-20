package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamTrade;

@Repository
public interface SteamTradeRepository extends JpaRepository<SteamTrade, String> {
}
