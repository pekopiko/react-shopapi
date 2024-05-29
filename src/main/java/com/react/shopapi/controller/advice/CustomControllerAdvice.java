package com.react.shopapi.controller.advice;

import com.react.shopapi.util.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

// @RestController 전용 예외처리 담당하는 어노테이션 (클래스레벨에 부착)
@RestControllerAdvice
public class CustomControllerAdvice {
    // 발생할 수 있는 각 예외들에 대한 처리는 메서드로 구분하여 구현
    // 이때 각 메서드가 예외발생시 실행되는 핸들러라는 뜻으로 @ExceptionHandler 어노테이션을 부착

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> notExist(NoSuchElementException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", msg));
        // 예외 발생시, 에러 메세지를 리턴 -> JSON 형태로리턴 -> 객체로 메세지 형성해서 body안에 넣기
        // -> 메세지를 담아줄 객체 형태가 필요 -> 번거로움
        // 그래서 메세지용 클래스 구지 안만들고, key=value형태를 띄는 Map으로 메세지객체 만들어 전달
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleIllegalArgsException(MethodArgumentNotValidException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg", msg));
    }

    // 우리가 만든 예외 발생시 실행될 핸드럴 추가
    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {
        String msg = e.getMessage();
        return ResponseEntity.ok().body(Map.of("error", msg));
    }



}
