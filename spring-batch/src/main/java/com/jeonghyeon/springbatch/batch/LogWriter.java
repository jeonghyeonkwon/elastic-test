package com.jeonghyeon.springbatch.batch;

import com.jeonghyeon.springbatch.entity.ChunkEntity;
import com.jeonghyeon.springbatch.entity.LogEntity;
import com.jeonghyeon.springbatch.repository.ChunkEntityRepository;
import com.jeonghyeon.springbatch.repository.LogEntityBulkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class LogWriter implements ItemWriter<String> {
    private static final String LOG_PATH = "/Users/kwonjeonghyeon/git-file";
    private final LogEntityBulkRepository logEntityBulkRepository;
    private final ChunkEntityRepository chunkEntityRepository;
    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends String> items) throws Exception {
        System.out.println(stepExecution.getCommitCount());
        List<LogEntity> logs = new ArrayList<>();

        String regex = "(\\S+) - (\\S+) \\[(.*?)\\] \"(.*?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        List<String> itemsList = (List<String>) items.getItems();
        for (int i = 0; i < itemsList.size(); i++) {
            String item = itemsList.get(i);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH + "/copy_" + stepExecution.getCommitCount() + ".log", true))) {

                writer.write(item);
                writer.newLine(); // 줄 바꿈

            } catch (IOException e) {
                throw new RuntimeException("Error writing to file", e);
            }

            Matcher matcher = pattern.matcher(item);
            if (matcher.find()) {
                String remoteAddr = matcher.group(1);
                String remoteUser = matcher.group(2);
                String timeLocal = matcher.group(3);
                String request = matcher.group(4);
                String status = matcher.group(5);
                String bodyBytesSent = matcher.group(6);
                String httpReferer = matcher.group(7);
                String httpUserAgent = matcher.group(8);
                logs.add(new LogEntity(remoteAddr, remoteUser, timeLocal, request, status, bodyBytesSent, httpReferer, httpUserAgent));
            }
        }

        ChunkEntity chunkEntity = new ChunkEntity();
        chunkEntity.mapping(logs);

        chunkEntityRepository.save(chunkEntity);
        logEntityBulkRepository.bulkInsert(logs);

    }
}
