package com.react.shopapi.repository;

import com.react.shopapi.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품1개 조회
    @EntityGraph(attributePaths = {"imageList"}) // fetch 조인으로 실행
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOneById(@Param("pno") Long pno);

    // 상품 삭제 -> delFlag만 수정
    // @Query가 INSERT, UPDATE, DELETE 쿼리문일경우 어노테이션 부착!!
    @Modifying(clearAutomatically = true)
    // -> DirtyChecking X -> 1차캐시 무시하고 바로처리 -> clearAuto..속성 true 지정해 영속성 비워주기
    @Query("update Product p set p.delFlag = :delFlag where p.pno = :pno")
    void updateDelFlag(@Param("pno") Long pno, @Param("delFlag") boolean delFlag);

    // 상품 목록 조회 + 대표이미지(ord=0)포함 + 삭제상품 제외(delFlag=true는 제외)
    // 조회하는 컬럼을 [p, pi] 배열로 만들고 레코드가 여러개라 컬럼배열을 다시 하나의 배열로 묶어서 리턴
    @Query("select p, pi from Product p left join p.imageList pi where pi.ord=0 and p.delFlag=false")
    Page<Object[]> selectList(Pageable pageable);


}
