package com.react.shopapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag;

    @ElementCollection // 컬렉션 값 타입이라고 명시: default lazy 로딩
    @Builder.Default  // builer 패턴 -> = ... 값대입 시 반드시 부착
    private List<ProductImage> imageList = new ArrayList<>();

    // 상품의 값을 수정할 수 있는 수정 메서드 직접 작성
    public void changePname(String pname) {
        this.pname = pname;
    }
    public void changePrice(int price) {
        this.price = price;
    }
    public void changeDesc(String pdesc) {
        this.pdesc = pdesc;
    }
    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }
    // ProductImage 타입으로 이미지 추가
    public void addImage(ProductImage image) {
        image.setOrd(this.imageList.size()); // ord값은 마지막 번호
        imageList.add(image);
    }
    // 문자열로 이미지 파일 추가
    public void addImageString(String fileName) {
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage); //위 addImage 메서드 호출해서 ord 까지 지정해 추가
    }
    // 이미지 리스트 내용물 전체 삭제
    public void clearImageList() {
        this.imageList.clear();
    }
}



