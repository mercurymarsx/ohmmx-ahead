package com.ohmmx.common.mapper;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohmmx.common.entity.TaskQueue;

@Repository
public interface TaskQueueRepository extends JpaRepository<TaskQueue, TaskQueue.ID> {

	@Query("FROM TaskQueue WHERE id.type like :type AND notBefore < NOW() AND notAfter > NOW() AND ttl > 0 " //
			+ "AND (lockedBy IS NULL OR lockedBy = :lockedBy) ORDER BY retries ")
	List<TaskQueue> findByType(@Param("type") String type, @Param("lockedBy") String lockedBy, Pageable pageable);

	@Query("FROM TaskQueue WHERE notBefore < NOW() AND notAfter > NOW() ORDER BY retries ")
	List<TaskQueue> findRunner(Pageable pageable);
}
