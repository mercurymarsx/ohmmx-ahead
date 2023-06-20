package com.ohmmx.common.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.WallpaperList;

@Repository
public interface WallpaperListRepository extends JpaRepository<WallpaperList, WallpaperList.ID> {
	@Query("DELETE FROM WallpaperList WHERE month = :month")
	@Modifying
	void deleteByMonth(@Param("month") String month);

	@Query("FROM WallpaperList WHERE month = :month")
	List<WallpaperList> findByMonth(@Param("month") String month);
}
