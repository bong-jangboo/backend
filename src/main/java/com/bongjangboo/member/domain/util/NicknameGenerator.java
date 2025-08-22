package com.bongjangboo.member.domain.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class NicknameGenerator {

    private static final List<String> ADJECTIVES = Arrays.asList(
            "친절한", "행복한", "빛나는", "용감한", "즐거운", "평화로운", "신비로운", "따뜻한"
    );

    private static final List<String> NOUNS = Arrays.asList(
            "라이언", "쿼카", "돌고래", "호랑이", "코끼리", "고양이", "강아지", "하늘"
    );

    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(random.nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }

    public static String generateNickname() {
        int randNumber = random.nextInt(4);
        return generate() + randNumber;
    }

}
