package com.react.shopapi.service;

import com.react.shopapi.domain.Todo;
import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.TodoDTO;
import com.react.shopapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Long add(TodoDTO todoDTO) {
        // todoDTO를 Entity로 변환해 Repository의 저장 기능 호출
        //Todo entity = todoDTO.toEntity();
        Todo entity = modelMapper.map(todoDTO, Todo.class); // (타겟객체, 변환하고싶은클래스타입)
        Todo savedEntity = todoRepository.save(entity);
        return savedEntity.getTno();
    }

    @Override
    public TodoDTO get(Long tno) {
        Todo findTodo = todoRepository.findById(tno).orElseThrow();
        // Entity -> DTO
        TodoDTO dto = modelMapper.map(findTodo, TodoDTO.class);
        return dto;
    }

    @Transactional
    @Override
    public void modify(TodoDTO todoDTO) {
        Todo findTodo = todoRepository.findById(todoDTO.getTno()).orElseThrow();
        findTodo.changeTitle(todoDTO.getTitle());
        findTodo.changeComplete(todoDTO.isComplete());
        findTodo.changeDueDate(todoDTO.getDueDate());
    }

    @Transactional
    @Override
    public void remove(Long tno) {
        Todo findTodo = todoRepository.findById(tno).orElse(null);
        if(findTodo != null) {
            todoRepository.delete(findTodo);
        }else {
            log.info("삭제 실패.....");
        }
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1, // 요청한페이지번호 (JPA에서는 0 page 부터 시작)
                pageRequestDTO.getSize(), // 한페이지에보여줄 글개수
                Sort.by("tno").descending()); // tno 역순 정렬

        Page<Todo> all = todoRepository.findAll(pageable);
        log.info("all : {}", all);

        // 글 목록
        List<TodoDTO> list = all.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());
        // 전체 글 개수
        long totalCount = all.getTotalElements();

        //PageResponseDTO<TodoDTO> responseDTO = new PageResponseDTO<>(list, pageRequestDTO, totalCount);

        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withList()
                .list(list)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();

        return responseDTO;
    }

}
