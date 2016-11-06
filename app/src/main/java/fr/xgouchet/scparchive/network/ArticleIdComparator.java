package fr.xgouchet.scparchive.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xavier Gouchet
 */
public class ArticleIdComparator implements Comparator<String> {

    public static final int FLAG_NULL = 0;
    public static final int FLAG_ARTICLE = 1;
    public static final int FLAG_JOKE = 2;
    public static final int FLAG_JOKE_NON_PARSABLE = 3;
    public static final int FLAG_EXPLAINED = 4;
    public static final int FLAG_ARCHIVE = 5;
    public static final int FLAG_UNKNOWN = 100;
    public static final Pattern PATTERN_NUMBER = Pattern.compile("[0-9]+");

    @Override public int compare(String s1, String s2) {
        int flags1 = flags(s1);
        int flags2 = flags(s2);

        if (flags1 != flags2) {
            return flags1 - flags2;
        }

        if (flags1 == FLAG_NULL) return 0;
        if (flags1 == FLAG_UNKNOWN) return s1.compareTo(s2);
        if (flags1 == FLAG_JOKE_NON_PARSABLE) return s1.compareTo(s2);

        return number(s1) - number(s2);
    }

    private int flags(@Nullable String s) {
        if (s == null) return FLAG_NULL;

        if (s.matches("^scp-[0-9]+$")) return FLAG_ARTICLE;
        if (s.matches("^scp-[0-9]+-j$")) return FLAG_JOKE;
        if (s.matches("^scp-.+-j$")) return FLAG_JOKE_NON_PARSABLE;
        if (s.matches("^scp-[0-9]+-ex")) return FLAG_EXPLAINED;
        if (s.matches("^scp-[0-9]+-arc$")) return FLAG_ARCHIVE;

        return FLAG_UNKNOWN;
    }

    private int number(@NonNull String s) {
        int result = -1;
        Matcher matcher = PATTERN_NUMBER.matcher(s);
        if (matcher.find()) {
            String match = matcher.group(0);
            result = Integer.parseInt(match);
        }
        return result;
    }

}
