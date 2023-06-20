package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamBundle;

@Repository
public interface SteamBundleRepository extends JpaRepository<SteamBundle, String> {
}
