package com.react.shopapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default // 빌더로 특정 필드 초기값 설정시 어노테이션 부착.
    private int page = 1;  // 요청 페이지 번호
    @Builder.Default
    private int size = 10;  // 한페이지에 보여줄 데이터의 개수

}
