package com.react.shopapi.service;

import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.TodoDTO;

public interface TodoService {
    // 할일 등록(저장) 기능
    Long add(TodoDTO todoDTO);
    // 조회
    TodoDTO get(Long tno);
    // 수정
    void modify(TodoDTO todoDTO);
    // 삭제
    void remove(Long tno);
    // 목록 조회
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
