package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamWorkset;

@Repository
public interface SteamWorksetRepository extends JpaRepository<SteamWorkset, Integer> {
}
