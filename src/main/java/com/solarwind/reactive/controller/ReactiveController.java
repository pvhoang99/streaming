package com.solarwind.reactive.controller;

import com.solarwind.reactive.handler.ExampleHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Zhongyang MA
 * Date: 2018/4/4
 * Time: 12:07
 */
@RestController
@RequestMapping("/annotated")
public class ReactiveController {

    @Autowired
    private ExampleHandler exampleHandler;

    // Example of returning a simple string to your browser
    @GetMapping("/test1")
    public Mono<String> test1() {
        return exampleHandler.test1();
    }

    // The server will push messages line by line to your browser - scrolling effect on your browser
    @GetMapping(value = "/test2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> test2() {
        Flux<Integer> integerFlux = exampleHandler.test2();
        return integerFlux;
    }

    @PostMapping(value = "/append")
    public void append(@RequestParam String content) throws IOException {
        File file = new File("C:\\\\Users\\\\HoangPV2\\\\Desktop\\\\New folder\\\\webflux-streaming-demo\\\\log.txt");
        try (BufferedWriter output = new BufferedWriter(new FileWriter(file.getName(), true))) {
            output.append(content);
            output.newLine();
        }

    }

    // Get all users from reactive mongoDB
//    @GetMapping(value = "/user/list", produces = "application/json")
//    public Flux<User> listAll() {
//        return exampleHandler.findAll();
//    }


}
