package com.ohmmx.common.mapper;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.WallpaperTrace;

@Repository
@CacheConfig(cacheNames = "wallpaperTrace")
public interface WallpaperTraceRepository extends JpaRepository<WallpaperTrace, String> {
	@Cacheable
	@Query("FROM WallpaperTrace WHERE month = :month")
	WallpaperTrace getByMonth(@Param("month") String month);

	@Query("FROM WallpaperTrace WHERE month = :month")
	WallpaperTrace getByMonthWithoutCache(@Param("month") String month);
}
