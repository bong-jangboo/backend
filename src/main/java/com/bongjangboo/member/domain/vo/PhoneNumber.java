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
public class PhoneNumber {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^010-?\\d{4}-?\\d{4}$");

    String value;

    public PhoneNumber(String value) {
        if (value == null || !PHONE_PATTERN.matcher(value).matches()) {
            throw new BusinessException(MemberErrorCode.PHONE_INVALID_FORMAT);
        }
        this.value = value.replace("-", "");
    }

}
