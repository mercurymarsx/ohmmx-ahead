package com.ohmmx.common.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.WallpaperTask;

@Repository
public interface WallpaperTaskRepository extends JpaRepository<WallpaperTask, WallpaperTask.ID> {
	@Query("DELETE FROM WallpaperTask WHERE month = :month")
	@Modifying
	void deleteByMonth(@Param("month") String month);

	@Query("FROM WallpaperTask WHERE month = :month")
	List<WallpaperTask> findListByMonth(@Param("month") String month);
}
