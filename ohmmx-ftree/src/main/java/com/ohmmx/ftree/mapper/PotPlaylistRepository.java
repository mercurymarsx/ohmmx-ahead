package com.ohmmx.ftree.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohmmx.ftree.entity.PotPlaylist;

@Repository
public interface PotPlaylistRepository extends JpaRepository<PotPlaylist, String> {

	@Query("FROM PotPlaylist WHERE filePath LIKE %:keyword% ORDER BY filePath ASC")
	List<PotPlaylist> findList(@Param("keyword") String keyword);
}
