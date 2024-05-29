package com.react.shopapi.repository;

import com.react.shopapi.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testInsert() {
        for(int i = 1; i <= 10; i++) {
            Product product = Product.builder()
                    .pname("상품" + i)
                    .price(100 * i)
                    .pdesc("상품설명 " + i)
                    .build();
            product.addImageString(UUID.randomUUID().toString() + "Image1.jpg");
            product.addImageString(UUID.randomUUID().toString() + "Image2.jpg");
            productRepository.save(product);
            log.info("------------------------------------------------------------");
        }
    }

    @Transactional
    @Test
    public void testRead() {
        Long pno = 1L;
        //Product product = productRepository.findById(pno).orElseThrow();
        Product product = productRepository.selectOneById(pno).orElseThrow();
        log.info("product : {}", product);
        log.info("product imageList :{}", product.getImageList());
    }

    @Commit // 영구적으로 DB에 반영
    @Transactional // update문
    @Test
    public void testDelete() {
        Long pno = 2L;
        productRepository.updateDelFlag(pno, true);
    }

    @Test
    public void testUpdate() {
        Long pno = 5L;
        Product product = productRepository.selectOneById(pno).orElseThrow();
        product.changePname("5번상품명 수정");
        product.changeDesc("5번 상품 설명 수정수정....");
        product.changePrice(50000);

        // 이미지 수정 : 기존 이미지 삭제하고 다시 추가
        product.clearImageList();
        product.addImageString(UUID.randomUUID().toString()+"NewImage1.jpg");
        product.addImageString(UUID.randomUUID().toString()+"NewImage2.jpg");
        product.addImageString(UUID.randomUUID().toString()+"NewImage3.jpg");

        productRepository.save(product); // DirtyChecking X -> merge로 처리
    }

    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
        log.info("result : {}", result.getContent()); // Object[ [Product, ProductImage], [Product, ProductImage], [Product, ProductImage],... ]
        result.getContent().forEach(obj -> log.info("obj : {}", Arrays.toString(obj)));
    }




}