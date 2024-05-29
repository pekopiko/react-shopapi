package com.react.shopapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.react.shopapi.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long tno;
    private String title;
    private String writer;
    private boolean complete;
    // 날짜를 화면에서 쉽게 처리하도록 JsonFormat 이용 -> 날짜 패턴 지정
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    /*
    // Entity -> TodoDTO
    public TodoDTO(Todo todo) {
        this.tno = todo.getTno();
        this.title = todo.getTitle();
        this.writer = todo.getWriter();
        this.complete = todo.isComplete(); // boolean 타입 -> getXxx(X) / isXxx(O)
        this.dueDate = todo.getDueDate();
    }
    // TodoDTO -> Entity
    public Todo toEntity() {
        Todo todo = Todo.builder()
                .title(title)
                .writer(writer)
                .complete(complete)
                .dueDate(dueDate)
                .build();
        return todo;
    }
    */
}
