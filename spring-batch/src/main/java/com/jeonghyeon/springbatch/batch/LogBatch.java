package com.jeonghyeon.springbatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class LogBatch {
    private static final String LOG_PATH = "/Users/kwonjeonghyeon/git-file/access.log";
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job processFileJob() {
        return new JobBuilder("processFileJob", jobRepository)
                .start(splitFileStep())
                .build();
    }

    @Bean
    public Step splitFileStep() {
        return new StepBuilder("splitFileStep", jobRepository)
                .<String, List<String>>chunk(1000, platformTransactionManager)
                .reader(fileItemReader())
                .writer(fileItemWriter())
                .build();
    }

    @Bean
    public ItemStreamReader<String> fileItemReader() {
        // 파일을 읽어오는 로직 구현


        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(LOG_PATH));
        reader.setLineMapper((line, lineNumber) -> new String(line));
        return reader;
    }
    @Bean
    public ItemWriter<List<String>> fileItemWriter() {
        return items -> {
            
            System.out.println(items);
        };
    }
}
