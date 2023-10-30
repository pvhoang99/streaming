package com.solarwind.reactive.handler;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class FileTailerConfiguration {


    @PostConstruct
    public void readFile() {
        Executor executor = Executors.newFixedThreadPool(10);
        TailerListener listener = new MyListener();
        File file = new File("C:\\Users\\HoangPV2\\Desktop\\New folder\\webflux-streaming-demo\\log.txt");
        try (Tailer tailer = new Tailer(file, listener, 1)) {
            executor.execute(tailer);
        }
    }


    public class MyListener extends TailerListenerAdapter {
        @Override
        public void handle(String line) {
            System.out.println(line);
        }
    }

}