package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamHandlerImage;

@Repository
public interface SteamHandlerImageRepository extends JpaRepository<SteamHandlerImage, Integer> {
}
