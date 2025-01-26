package com.jeonghyeon.springbatch.repository;

import com.jeonghyeon.springbatch.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntityRepository extends JpaRepository<LogEntity, Long> {
}
