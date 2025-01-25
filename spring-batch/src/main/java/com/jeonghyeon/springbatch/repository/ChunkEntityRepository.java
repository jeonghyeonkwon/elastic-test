package com.jeonghyeon.springbatch.repository;

import com.jeonghyeon.springbatch.entity.ChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkEntityRepository extends JpaRepository<ChunkEntity, Long> {
}
