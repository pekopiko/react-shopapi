package com.react.shopapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    public void builderPattern() {

        // Member 객체 생성해 변수의 값체우기
        // #1. 생성자 -> 단점 : 변수가 많으면 매개변수가 길어짐 -> 개발자 실수 유발
        //Member member = new Member(1L, "이름", "주소");
        // #2. Setter 이용 -> 단점 : setter를 오픈 -> 불변성 보장X
        //Member member1 = new Member();
        //member1.setId(2L);
        //member1.setName("이름2");
        //member1.setAddress("주소2");
        // #3. 빌더패턴
        //Member member2 = Member.builder() // Member안에 있는 내부클래스 Builder 객체생성된것 리턴
          //      .id(3L)
            //    .name("이름3")
              //  .address("주소3").build();


    }
}