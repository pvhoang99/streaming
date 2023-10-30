package com.solarwind.reactive.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file-changes")
public class FileChangesController {


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFileChanges() {
        String path = "C:\\\\Users\\\\HoangPV2\\\\Desktop\\\\New folder\\\\webflux-streaming-demo\\\\log.txt";
        File file = new File(path);
        int numLinesToRead = 5;


        Flux<String> result = Flux.create(emitter -> {
            List<String> lastLines = readLastLines(path, numLinesToRead);
            for (String line : lastLines) {
                emitter.next(line);
            }
            TailerListenerAdapter listener = new TailerListenerAdapter() {
                @Override
                public void handle(String line) {
                    emitter.next(line);
                }
            };
            Tailer tailer = new Tailer(file, listener, 500, true);
            Thread tailerThread = new Thread(tailer);
            tailerThread.start();

            emitter.onDispose(() -> {
                tailer.stop();
                tailerThread.interrupt();
            });
        });

        return result;
    }

    private static List<String> readLastLines(String filePath, int numLines) {
        List<String> lines;
        try {
            lines = FileUtils.readLines(new File(filePath), "UTF-8");
        } catch (IOException e) {
            return new ArrayList<>();
        }
        int start = Math.max(0, lines.size() - numLines);
        return lines.subList(start, lines.size());
    }
}
