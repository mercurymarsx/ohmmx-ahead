package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamCategory;

@Repository
public interface SteamCategoryRepository extends JpaRepository<SteamCategory, String> {
}
