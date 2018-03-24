package com.lexu.testershatethem;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by lexu on 24.03.2018.
 */

public class CommonUtils {

    static boolean validEmail(String str) {
        return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), str);
    }

    static boolean validPhone(String str) {
        return Pattern.matches(Patterns.PHONE.pattern(), str);
    }

    static boolean validName(String str) {
        return Pattern.matches("^[\\\\p{L} .'-]+$", str);
    }

    static boolean validPassword(String str1, String str2) {
        return str1.contentEquals(str2) && str1.length() >= 5;
    }
}
