package com.solarwind.reactive.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * User: Zhongyang MA
 * Date: 2018/4/11
 * Time: 17:28
 */
@Component
public class ExampleHandler {


    @Autowired
    private FileService fileService;

    /**
     * for annotated controllers
     */
    public Mono<String> test1() {
        return Mono.just("test1: return a simple string");
    }

    public Flux<Integer> test2() {
        return Flux.fromStream(this.fileService.readFile());
    }


    /**
     * below for functional routers
     */
    public Mono<ServerResponse> funcTest1(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("funcTest1: WebFlux functional router."));
    }

    public Mono<ServerResponse> funcTest2(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("funcTest2: WebFlux functional router."));
    }


    ;

}
