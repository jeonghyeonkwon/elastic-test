package com.jeonghyeon.springbatch.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "T_CHUNK")
public class ChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chunk_id")
    private Long id;

    @OneToMany(mappedBy = "chunkEntity")
    private List<LogEntity> logEntity  = new ArrayList<>();


    public ChunkEntity() {

    }

    public void mapping(List<LogEntity> logEntity){
        logEntity.forEach(log -> log.mapping(this));
    }
}
