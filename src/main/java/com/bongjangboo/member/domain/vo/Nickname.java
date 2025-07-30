package com.bongjangboo.member.domain.vo;

import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class Nickname {
    String value;

    // TODO : 도메인 코드 nickname 타입 VO로 변경

    public Nickname(String value) {
        if(value ==null || value.trim().isEmpty())
            throw new BusinessException(MemberErrorCode.NICKNAME_INVALID_FORMAT);
        this.value = value;
    }


    // 추후 닉네임과 관련된 정책 추가 가능

}
