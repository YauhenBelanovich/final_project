package com.gmail.yauhen2012.service.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.yauhen2012.service.constant.PasswordGenerationConstant.CHAR_LOWER;
import static com.gmail.yauhen2012.service.constant.PasswordGenerationConstant.CHAR_UPPER;
import static com.gmail.yauhen2012.service.constant.PasswordGenerationConstant.NUMBER;
import static com.gmail.yauhen2012.service.constant.PasswordGenerationConstant.PASSWORD_LENGHT;

public class PasswordGeneratorUtil {

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final String PASSWORD_ALLOW_BASE_SHUFFLE = shuffleString(PASSWORD_ALLOW_BASE);
    private static final String PASSWORD_ALLOW = PASSWORD_ALLOW_BASE_SHUFFLE;

    private static SecureRandom random = new SecureRandom();

    public static String generateRandomPassword() {
        int length = PASSWORD_LENGHT;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(PASSWORD_ALLOW.length());
            char rndChar = PASSWORD_ALLOW.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    private static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }
}
