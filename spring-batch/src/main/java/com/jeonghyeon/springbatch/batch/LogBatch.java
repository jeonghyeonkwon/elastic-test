package com.jeonghyeon.springbatch.batch;

import com.jeonghyeon.springbatch.entity.ChunkEntity;
import com.jeonghyeon.springbatch.entity.LogEntity;
import com.jeonghyeon.springbatch.repository.ChunkEntityRepository;
import com.jeonghyeon.springbatch.repository.LogEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class LogBatch {
    private static final String LOG_PATH = "/Users/kwonjeonghyeon/git-file/access_3.log";
    private final JobRepository jobRepository;
    private final LogEntityRepository logEntityRepository;
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
                .<String, LogEntity>chunk(10, platformTransactionManager)
                .reader(fileItemReader())
                .processor(fileItemProcessor())
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
    public ItemProcessor<String, LogEntity> fileItemProcessor() {
        return logStr -> {

            String regex = "(\\S+) - (\\S+) \\[(.*?)\\] \"(.*?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(logStr);

            if (matcher.find()) {
                String remoteAddr = matcher.group(1);
                String remoteUser = matcher.group(2);
                String timeLocal = matcher.group(3);
                String request = matcher.group(4);
                String status = matcher.group(5);
                String bodyBytesSent = matcher.group(6);
                String httpReferer = matcher.group(7);
                String httpUserAgent = matcher.group(8);
                return new LogEntity(remoteAddr, remoteUser, timeLocal, request, status, bodyBytesSent, httpReferer, httpUserAgent);


            }
            System.out.println("찾지 못함");
            System.out.println(logStr);
            return null;
        };
    }

    @Bean
    public ItemWriter<LogEntity> fileItemWriter() {
        return items -> {
            ChunkEntity chunkEntity = new ChunkEntity();

            List<LogEntity> logs = (List<LogEntity>) items.getItems();
            chunkEntity.mapping(logs);

            chunkEntityRepository.save(chunkEntity);
            logEntityRepository.saveAll(logs);

            System.out.println("@@@@@@");
        };
    }
}
