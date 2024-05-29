package com.react.shopapi.service;

import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.ProductDTO;

public interface ProductService {

    // 상품 목록 조회
    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);
    // 상품 등록 처리
    Long add(ProductDTO productDTO);
    // 상품 조회
    ProductDTO get(Long pno);
    // 상품 수정
    ProductDTO modifyFiles(ProductDTO productDTO);
    void modify(ProductDTO productDTO);
    // 상품 삭제
    void remove(Long pno);

}
