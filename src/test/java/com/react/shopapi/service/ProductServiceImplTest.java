package com.react.shopapi.service;

import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductServiceImplTest {

    @Autowired
    ProductService productService;

    @Test
    public void testList() {
        // PageRequestDTO 필요
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build(); // default 1, 10
        PageResponseDTO<ProductDTO> list = productService.getList(pageRequestDTO);
        log.info("list : {}", list); // pageResponseDTO 출력
        list.getList().forEach(dto -> log.info("dto : {}", dto)); // 실제 목록 출력
    }

    @Test
    public void testAdd() {
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새 상품")
                .pdesc("새 상품 설명...")
                .price(12340)
                .build();
        // 가짜 : 저장된 이미지 파일명으로 테스트
        productDTO.setUploadedFileNames(
                List.of(
                        UUID.randomUUID().toString() + "Test1.jpg",
                        UUID.randomUUID().toString() + "Test2.jpg"
                ));
        productService.add(productDTO);
    }

    @Test
    public void testGet() {
        Long pno = 10L;
        ProductDTO productDTO = productService.get(pno);
        log.info("productDTO :{}", productDTO);
        log.info("uploadFileNames : {}", productDTO.getUploadedFileNames());
    }


}