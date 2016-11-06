package fr.xgouchet.scparchive.model;

import android.text.Html;

import static fr.xgouchet.scparchive.commons.SpannedUtils.trim;

/**
 * @author Xavier Gouchet
 */
public class Photo extends Image {

    private final String htmlCaption;

    public Photo(String url, String htmlCaption) {
        super(url);
        this.htmlCaption = htmlCaption;
    }

    public String getHtmlCaption() {
        return htmlCaption;
    }

    public CharSequence getCaption() {
        return trim(Html.fromHtml(htmlCaption));
    }


}
