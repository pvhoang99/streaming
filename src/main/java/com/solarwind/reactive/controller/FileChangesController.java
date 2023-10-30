package com.solarwind.reactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

@RestController
@RequestMapping("/file-changes")
public class FileChangesController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFileChanges() {

        File file = new File("C:\\Users\\HoangPV2\\Desktop\\New folder\\webflux-streaming-demo\\log.txt");
        Path path = file.toPath();

        Flux<String> fileChangesFlux = Flux.<String>create(emitter -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                while (!emitter.isCancelled()) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals(path.getFileName().toString())) {
                            String fileContent = readLastLine(file);
                            emitter.next(fileContent);
                        }
                    }
                    key.reset();
                }
            } catch (IOException | InterruptedException e) {
                emitter.error(e);
            }
        });

        System.out.println("here");

        return fileChangesFlux;
    }

    private String readLastLine(File file) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            long fileLength = randomAccessFile.length();
            if (fileLength == 0) {
                return ""; // File is empty
            }
            long position = fileLength - 1;
            StringBuilder lastLineBuilder = new StringBuilder();

            // Loop backward from the end of the file until a newline character is found
            while (position >= 0 && lastLineBuilder.length() == 0) {
                randomAccessFile.seek(position);
                char currentChar = (char) randomAccessFile.readByte();
                if (currentChar == '\n') {
                    // Found newline character, read the last line
                    byte[] bytes = new byte[(int) (fileLength - position - 1)];
                    randomAccessFile.readFully(bytes);
                    lastLineBuilder.append(new String(bytes));
                }
                position--;
            }

            return lastLineBuilder.reverse().toString();
        }
    }
}