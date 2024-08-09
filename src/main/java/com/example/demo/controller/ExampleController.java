package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        log.info("예제 메시지 e : {}", "message");
        throw new IllegalStateException("컨트롤러에서 에러가 발생함", new RuntimeException("외부 시스템에서 에러가 발생함"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        String causeMessage = ex.getCause() == null ? null : ex.getCause().getMessage();
        String causeClassName = ex.getCause() == null ? null : ex.getCause().getClass().getSimpleName();

        log.warn(
                "예외 메시지 : {}, 예외 클래스 : {}, 예외 원인 메시지 : {}, 예외 원인 클래스 : {}"
                , message, ex.getClass().getSimpleName()
                , causeMessage, causeClassName
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
