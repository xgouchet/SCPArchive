package fr.xgouchet.scparchive.commons;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.style.URLSpan;

import fr.xgouchet.scparchive.network.ArticleRepository;

/**
 * @author Xavier Gouchet
 */
public class SpannedUtils {

    @NonNull
    public static CharSequence trim(@Nullable CharSequence source) {

        if (source == null) return "";

        final int length = source.length();
        int start = -1;
        int end = length;

        // loop to the first non-whitespace character
        while ((++start < length) && Character.isWhitespace(source.charAt(start))) {
        }

        // loop back to the first non-whitespace character
        while ((--end >= 0) && Character.isWhitespace(source.charAt(end))) {
        }

        if (start >= end) return "";

        return source.subSequence(0, end + 1);
    }

    @NonNull
    public static CharSequence linkify(@Nullable CharSequence text) {
        if (text == null) return "";

        SpannableString spannable;
        if (text instanceof SpannableString) {
            spannable = (SpannableString) text;
        } else {
            spannable = new SpannableString(text);
        }

        URLSpan[] spans = spannable.getSpans(0, text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            String url = span.getURL();
            if (url.startsWith("http://")
                    || url.startsWith("https://")) continue;


            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            int flags = spannable.getSpanFlags(span);
            String newUrl;
            if (url.startsWith("javascript")) {
                newUrl = "";
            } else {
                newUrl = ArticleRepository.BASE_URL + url;
            }

            spannable.removeSpan(span);
            spannable.setSpan(new URLSpan(newUrl), start, end, flags);

        }

        return spannable;
    }


    @NonNull
    public static CharSequence withTypeface(@Nullable CharSequence text, @NonNull Typeface typeface) {
        if (text == null) return "";

        SpannableString spannable;
        if (text instanceof SpannableString) {
            spannable = (SpannableString) text;
        } else {
            spannable = new SpannableString(text);
        }

        spannable.setSpan(new TypefaceSpan(typeface),
                0,
                spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static final class TypefaceSpan extends MetricAffectingSpan {

        private final Typeface typeface;

        public TypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(typeface);

            // Note: This flag is required for proper typeface rendering
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(typeface);

            // Note: This flag is required for proper typeface rendering
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
