package com.ohmmx.ftree.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohmmx.ftree.entity.Fc2ppv;

@Repository
public interface Fc2ppvRepository extends JpaRepository<Fc2ppv, Long> {

	@Query("FROM Fc2ppv WHERE fc2name = :name AND monthly = :month ORDER BY ID DESC LIMIT 1")
	Fc2ppv getByFcid(@Param("name") String name, @Param("month") String month);
}
