package com.react.shopapi.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// UserDetails 인터페이스와 호환되도록 구현 (자식 클래스가 됨)
public class MemberDTO extends User {

    private String email;
    private String password;
    private String nickname;
    private boolean social;
    private List<String> roleNames = new ArrayList<>();

    // 생성자
    public MemberDTO(String email, String password, String nickname, boolean social, List<String> roleNames) {
        super(email, password, roleNames.stream()
                .map(str -> new SimpleGrantedAuthority("ROLE_" + str))
                .collect(Collectors.toList()));
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    // 현재 사용자 정보를 Map 타입으로 리턴 : JWT 를 위한 메서드 -> 추후 JWT 문자열 생성시 사용
    // MemberDTO -> Map<String,Object> 타입으로 변환해서 리턴
    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password); // 비번은 나중에 전달 안하는 것으로 변경. 지금은 확인차 추가
        map.put("nickname", nickname);
        map.put("social", social);
        map.put("roleNames", roleNames); // List<String> 타입
        return map;
    }


}
