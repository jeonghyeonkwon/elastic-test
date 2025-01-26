package com.jeonghyeon.springbatch.repository;

import com.jeonghyeon.springbatch.entity.LogEntity;

import java.util.List;

public interface LogEntityBulkRepository {
    void bulkInsert(List<LogEntity> logs);
}
