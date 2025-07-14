package com.example.jangboo.member.domain.vo;

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

    /**
     * Constructs a PhoneNumber instance after validating the input string.
     *
     * @param value the phone number string to be validated and stored
     * @throws IllegalArgumentException if the input is null or does not match the required phone number format (e.g., "010-1234-5678" or "01012345678")
     */
    public PhoneNumber(String value) {
        if (value == null || !PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid phone number format. ex) 010-1234-5678");
        }
        this.value = value;
    }

}
