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
@ToString(exclude = "roleList")
public class Member {
    @Id
    private String email;  // id는 email
    private String password;
    private String nickname;
    private boolean social; //  소셜 회원 여부
    // 권한 목록 (테이블 별도로 생성, default fetch lazy)
    @ElementCollection
    @Builder.Default
    @Enumerated(EnumType.STRING) // enum 문자열로 들어가도록
    private List<Role> roleList = new ArrayList<>();

    // 필드 수정 메서드
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changePassword(String password) {
        this.password = password;
    }
    public void changeSocial(boolean social) {
        this.social = social;
    }

    // 권한 추가
    public void addRole(Role role) {
        roleList.add(role);
    }
    // 권한 모두 삭제
    public void clearRole() {
        roleList.clear();
    }

}
