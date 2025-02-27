package me.notpseudo.lexiclient.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to format text
 *
 * Credit to SkyblockAddons
 */
public class TextUtils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORZ]");
    private static final Pattern MAGNITUDE_PATTERN = Pattern.compile("(\\d[\\d,.]*\\d*)+([kKmMbBtT])");
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.US);

    public static String stripColor(final String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String convertMagnitudes(String text) throws ParseException {
        Matcher matcher = MAGNITUDE_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            double parsedDouble = NUMBER_FORMAT.parse(matcher.group(1)).doubleValue();
            String magnitude = matcher.group(2).toLowerCase(Locale.ROOT);

            switch (magnitude) {
                case "k":
                    parsedDouble *= 1_000;
                    break;
                case "m":
                    parsedDouble *= 1_000_000;
                    break;
                case "b":
                    parsedDouble *= 1_000_000_000;
                    break;
                case "t":
                    parsedDouble *= 1_000_000_000_000L;
            }

            matcher.appendReplacement(sb, NUMBER_FORMAT.format(parsedDouble));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    public static String getColorCode(int colorOption) {
        if (colorOption < 10 && colorOption > -1) return "§" + colorOption;
        char color = (char) ('a' + (colorOption - 10));
        return "§" + color;
    }

}
