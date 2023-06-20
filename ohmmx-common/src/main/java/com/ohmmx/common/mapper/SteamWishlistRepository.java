package com.ohmmx.common.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.SteamWishlist;

@Repository
public interface SteamWishlistRepository extends JpaRepository<SteamWishlist, Integer> {
}
