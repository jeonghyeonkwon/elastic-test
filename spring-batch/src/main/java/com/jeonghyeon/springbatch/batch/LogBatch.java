package com.jeonghyeon.springbatch.batch;

import com.jeonghyeon.springbatch.entity.LogEntity;
import com.jeonghyeon.springbatch.repository.ChunkEntityRepository;
import com.jeonghyeon.springbatch.repository.LogEntityBulkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LogBatch {
    private static final String LOG_PATH = "/Users/kwonjeonghyeon/git-file";
    private final JobRepository jobRepository;
    private final LogEntityBulkRepository logEntityBulkRepository;
    private final ChunkEntityRepository chunkEntityRepository;
    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job processFileJob() {
        return new JobBuilder("processFileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFileStep())
                .build();
    }

    @Bean
    public Step splitFileStep() {
        return new StepBuilder("splitFileStep", jobRepository)
                .<String, LogEntity>chunk(1000, platformTransactionManager)
                .reader(fileItemReader())
                .writer(fileItemWriter())
                .build();
    }


    @Bean
    public ItemStreamReader<String> fileItemReader() {
        // 파일을 읽어오는 로직 구현
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(LOG_PATH + "/access_1.log"));
        reader.setLineMapper((line, lineNumber) -> line);
        return reader;


    }

    @Bean
    public ItemWriter fileItemWriter() {
        return new LogWriter(logEntityBulkRepository, chunkEntityRepository);
    }
}
