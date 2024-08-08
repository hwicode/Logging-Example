package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ExampleController {

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public String getExample() {
        log.info("예제 메시지 : {}", "message");
        return "ok";
    }

    @GetMapping("/e")
    public String causeError() {
        log.info("예제 메시지 : {}", "message");
        throw new IllegalStateException(new IllegalArgumentException(new RuntimeException("컨트롤러에서 에러가 발생함")));
    }
}
