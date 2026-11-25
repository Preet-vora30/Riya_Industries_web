package com.riyaindustries.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");

    private SlugUtil() {
    }

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "item";
        }
        String normalized = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        String noWhitespace = WHITESPACE.matcher(normalized).replaceAll("-");
        String slug = NON_LATIN.matcher(noWhitespace).replaceAll("")
            .toLowerCase(Locale.ROOT)
            .replaceAll("-{2,}", "-")
            .replaceAll("^-|-$", "");
        return slug.isEmpty() ? "item" : slug;
    }
}
