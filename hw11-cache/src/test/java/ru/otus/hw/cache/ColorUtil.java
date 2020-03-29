package ru.otus.hw.cache;

class ColorUtil {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    static String fillTextColor(String string, boolean isGreen) {
        return isGreen ? (ANSI_GREEN + string + ANSI_RESET) : (ANSI_RED + string + ANSI_RESET);
    }
}
