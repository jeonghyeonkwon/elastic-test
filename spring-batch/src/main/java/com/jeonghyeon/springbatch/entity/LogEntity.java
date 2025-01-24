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
    private String request;
    private String status;
    private String bytesSent;
    private String httpReferer;
    private String httpUserAgent;
    private String httpXForwardedFor;

    protected LogEntity(){}

    public LogEntity(String remoteAddr, String remoteUser, String timeLocal, String request, String status, String bytesSent, String httpReferer, String httpUserAgent, String httpXForwardedFor) {
        this.remoteAddr = remoteAddr;
        this.remoteUser = remoteUser;
        this.timeLocal = timeLocal;
        this.request = request;
        this.status = status;
        this.bytesSent = bytesSent;
        this.httpReferer = httpReferer;
        this.httpUserAgent = httpUserAgent;
        this.httpXForwardedFor = httpXForwardedFor;
    }
}

