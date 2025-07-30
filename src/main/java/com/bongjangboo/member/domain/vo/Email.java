package com.bongjangboo.member.domain.vo;

import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.regex.Pattern;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    String value;

    public Email(String value) {
        if (value == null || value.isEmpty() ||  !EMAIL_PATTERN.matcher(value).matches()) {
            throw new BusinessException(MemberErrorCode.EMAIL_INVALID_FORMAT);
        }
        this.value = value.trim();
    }




    /**
     * 도메인 검증 (특정 도메인 확인)
     */
    public boolean isDomain(String domain) {
        return value.endsWith("@" + domain);
    }

    /**
     * 이메일의 로컬 부분 반환 (@ 앞 부분)
     */
    public String getLocalPart() {
        int atIndex = value.indexOf("@");
        return atIndex > 0 ? value.substring(0, atIndex) : "";
    }

    /**
     * 이메일의 도메인 부분 반환 (@ 뒤 부분)
     */
    public String getDomainPart() {
        int atIndex = value.indexOf("@");
        return atIndex >= 0 && atIndex < value.length() - 1 ? 
               value.substring(atIndex + 1) : "";
    }
}
