package com.react.shopapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable // 값타입임을 명시
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;
    @Setter // 편의를 위해 ord만 setter 걸어주기
    private int ord;  // 이미지마다 번호 지정, 대표이미지 = 0 인것

}
