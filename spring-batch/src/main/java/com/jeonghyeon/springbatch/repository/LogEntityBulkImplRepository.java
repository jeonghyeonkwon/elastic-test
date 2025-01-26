package com.jeonghyeon.springbatch.repository;

import com.jeonghyeon.springbatch.entity.LogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class LogEntityBulkImplRepository implements LogEntityBulkRepository {
    private final JdbcTemplate dataJdbcTemplate;

    @Override
    @Transactional
    public void bulkInsert(List<LogEntity> logs) {
        String sql = "INSERT INTO T_LOG(chunk_pk, bytesSent, httpReferer, httpUserAgent, remoteAddr, remoteUser, status, timeLocal, request)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        dataJdbcTemplate.batchUpdate(sql,
                logs,
                logs.size(),
                (PreparedStatement ps, LogEntity log) -> {
                    ps.setLong(1, log.getChunkEntity().getId());
                    ps.setString(2, log.getBytesSent());
                    ps.setString(3, log.getHttpReferer());
                    ps.setString(4, log.getHttpUserAgent());
                    ps.setString(5, log.getRemoteAddr());
                    ps.setString(6, log.getRemoteUser());
                    ps.setString(7, log.getStatus());
                    ps.setString(8, log.getTimeLocal());
                    ps.setString(9, log.getRequest());
                });
    }
}
