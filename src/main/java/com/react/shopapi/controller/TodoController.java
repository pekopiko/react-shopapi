package com.react.shopapi.controller;

import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.TodoDTO;
import com.react.shopapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/todo")
@Slf4j
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // tno로 조회 요청
    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno) {
        log.info("******** TodoController GET /:tno - tno : {}", tno);
        TodoDTO todoDTO = todoService.get(tno);
        return todoDTO;
    }

    // 목록 요청
    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("******* TodoController GET /list - pageRequestDTO : {}", pageRequestDTO);
        PageResponseDTO<TodoDTO> list = todoService.list(pageRequestDTO);
        return list;
    }

    // 등록 요청
    @PostMapping("/")
    public Map<String, Long> add(@RequestBody TodoDTO todoDTO) {
        log.info("******** TodoController POST /add - todoDTO : {}", todoDTO);
        Long tno = todoService.add(todoDTO);
        return Map.of("tno", tno);
    }

    // 수정 요청
    @PutMapping("/{tno}")
    public Map<String, String> modify(
            @PathVariable("tno") Long tno,
            @RequestBody TodoDTO todoDTO) {

        log.info("******** TodoController PUT /modify - tno : {}", tno);
        log.info("******** TodoController PUT /modify - todoDTO : {}", todoDTO);

        todoDTO.setTno(tno); // todoDTO에 path variable로 꺼낸 tno 추가
        todoService.modify(todoDTO);
        return Map.of("Result", "SUCCESS");
    }

    // 삭제 요청
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno) {
        log.info("******** TodoController DELETE /remove - tno : {}", tno);
        todoService.remove(tno);
        return Map.of("Result", "SUCCESS");
    }



}
