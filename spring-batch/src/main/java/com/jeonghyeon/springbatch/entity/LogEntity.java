package com.jeonghyeon.springbatch.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "T_LOG")
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remoteAddr;
    private String remoteUser;
    private String timeLocal;
    @Lob
    private String request;
    private String status;
    private String bytesSent;
    @Lob
    private String httpReferer;
    @Lob
    private String httpUserAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chunk_pk")
    private ChunkEntity chunkEntity;

    protected LogEntity() {
    }

    public LogEntity(String remoteAddr, String remoteUser, String timeLocal, String request, String status, String bytesSent, String httpReferer, String httpUserAgent) {
        this.remoteAddr = remoteAddr;
        this.remoteUser = remoteUser;
        this.timeLocal = timeLocal;
        this.request = request;
        this.status = status;
        this.bytesSent = bytesSent;
        this.httpReferer = httpReferer;
        this.httpUserAgent = httpUserAgent;
    }


    @Override
    public String toString() {
        return "LogEntity{" +
                "id=" + id +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", remoteUser='" + remoteUser + '\'' +
                ", timeLocal='" + timeLocal + '\'' +
                ", request='" + request + '\'' +
                ", status='" + status + '\'' +
                ", bytesSent='" + bytesSent + '\'' +
                ", httpReferer='" + httpReferer + '\'' +
                ", httpUserAgent='" + httpUserAgent + '\'' +
                '}';
    }

    public void mapping(ChunkEntity chunkEntity) {
        this.chunkEntity = chunkEntity;
        chunkEntity.getLogEntity().add(this);
    }
}

