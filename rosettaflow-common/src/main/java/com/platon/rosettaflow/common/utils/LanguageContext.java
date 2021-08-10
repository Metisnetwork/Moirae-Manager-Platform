package com.platon.rosettaflow.common.utils;

/**
 * @author admin
 * @date 2021/7/20
 */
public class LanguageContext {
    private static final ThreadLocal<String> LANGUAGE_LOCAL = new ThreadLocal<>();

    private LanguageContext() {
    }

    public static String get() {
        return LANGUAGE_LOCAL.get();
    }

    public static void set(String language) {
        LANGUAGE_LOCAL.remove();
        LANGUAGE_LOCAL.set(language);
    }

    public static void remove() {
        LANGUAGE_LOCAL.remove();
    }
}
