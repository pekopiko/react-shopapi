package com.react.shopapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long pno;           // 상품고유번호
    private String pname;        // 상품명
    private int price;          // 상품 가격
    private String pdesc;       // 상품 설명
    private boolean delFlag;    // 상품 삭제 여부 : 삭제 = true

    // 업로드할 파일들 담을 리스트
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();
    // 업로드 완료된 파일 이름들 리스트
    @Builder.Default
    private List<String> uploadedFileNames = new ArrayList<>();
}
