package com.solarwind.reactive.handler;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Stream;

@Component
public class FileService {

    public Stream<Integer> readFile() {
       return Stream.generate(() -> {
            try {
                Thread.sleep(1000); // Delay 1 giây
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Random().nextInt();
        });
    }

}
