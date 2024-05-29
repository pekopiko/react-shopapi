package com.react.shopapi.service;

import com.react.shopapi.domain.Member;
import com.react.shopapi.dto.MemberDTO;
import com.react.shopapi.dto.MemberModifyDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {

    // 카카오에 회원정보 요청
    MemberDTO getKakaoMember(String accessToken);

    // 회원 정보 수정 처리
    void modifyMember(MemberModifyDTO memberModifyDTO);

    // Member Entity -> MemberDTO
    default MemberDTO entityToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.isSocial(),
                member.getRoleList()
                        .stream()
                        .map(role -> role.name())
                        .collect(Collectors.toList()));
        return memberDTO;
    }

}
