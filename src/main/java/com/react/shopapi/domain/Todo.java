package com.react.shopapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor // Builder 때문에 추가
@NoArgsConstructor  // JPA 엔티티라서 추가
public class Todo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;       // 고유번호
    private String title;   // 할일내용
    private String writer;  // 작성자
    private boolean complete;   // 할일 완료 여부
    private LocalDate dueDate;  // 할일 날짜

    // 수정 가능한 필드를 위한 수정 메서드 추가
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeComplete(boolean complete) {
        this.complete = complete;
    }
    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
