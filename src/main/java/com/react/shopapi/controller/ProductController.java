package com.react.shopapi.controller;

import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.ProductDTO;
import com.react.shopapi.service.ProductService;
import com.react.shopapi.util.FileUtilCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final FileUtilCustom fileUtil;
    private final ProductService productService;

    // 상품 등록
    @PostMapping("/")
    public Map<String, Long> add(ProductDTO productDTO) {
        log.info("********** ProductController POST /add - productDTO : {}", productDTO);

        Long pno = productService.add(productDTO);

        /*try {
            Thread.sleep(2000); // 모달창 보기위해 잠시 지연시키기
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        return Map.of("RESULT", pno);
    }

    // 상품 이미지 요청
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    // 상품 목록 요청
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("*********** ProductController GET /list - pageRequestDTO : {}", pageRequestDTO);
        return productService.getList(pageRequestDTO);
    }

    // 상품 조회
    @GetMapping("/{pno}")
    public ProductDTO get(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    // 상품 수정 요청
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);
        log.info("****** ProductController PUT - productDTO : {}", productDTO);
        // 서비스의 수정 파일 처리 호출
        ProductDTO dto = productService.modifyFiles(productDTO);
        log.info("****** ProductController PUT - dto : {}", dto);
        // 서비스의 수정 처리 호출
        productService.modify(dto);

        return Map.of("RESULT", "SUCCESS");
    }

    // 상품 삭제 요청
    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {
        productService.remove(pno);
        return Map.of("RESULT", "SUCCESS");
    }

}
