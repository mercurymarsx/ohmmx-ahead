package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamThirdToolsCard;

@Repository
public interface SteamThirdToolsCardRepository extends JpaRepository<SteamThirdToolsCard, Integer> {
}
