package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamHandlerDlc;

@Repository
public interface SteamHandlerDlcRepository extends JpaRepository<SteamHandlerDlc, Integer> {
}
