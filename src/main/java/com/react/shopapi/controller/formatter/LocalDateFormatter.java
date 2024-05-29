package com.react.shopapi.controller.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// 포매팅 할때 사용될 포매터 작성 -> 적용되도록 설정 추가 필요
public class LocalDateFormatter implements Formatter<LocalDate> {
    // 문자열날짜 -> LocalDate 타입으로 변환
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        // 2025/05/02 -> "yyyy/MM/dd", 2025-05-02 -> "yyyy-MM-dd"
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    // LocalDate -> 문자열날짜
    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    }
}
