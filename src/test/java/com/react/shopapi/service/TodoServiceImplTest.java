package com.react.shopapi.service;

import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.TodoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TodoServiceImplTest {

    @Autowired
    private TodoService todoService;

    @Test
    public void testAdd() {
        // 저장 테스트 해볼 TodoDTO 필요
        TodoDTO todoDTO = TodoDTO.builder()
                .title("서비스 테스트")
                .writer("testUser")
                .dueDate(LocalDate.of(2024, 5, 5))
                .build();
        log.info("todoDTO : {}", todoDTO);

        // 저장!
        Long savedTno = todoService.add(todoDTO);
        log.info("savedTno :{}", savedTno);
    }

    @Test
    public void getTest() {
        TodoDTO todoDTO = todoService.get(1L);
        log.info("find dto : {}", todoDTO);
    }

    @Test
    public void listTest() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<TodoDTO> response = todoService.list(pageRequestDTO);
        log.info("response : {}", response);
    }




}