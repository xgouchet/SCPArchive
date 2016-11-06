package fr.xgouchet.scparchive.model;

import android.text.Html;

import static fr.xgouchet.scparchive.commons.SpannedUtils.linkify;
import static fr.xgouchet.scparchive.commons.SpannedUtils.trim;

/**
 * @author Xavier Gouchet
 */
public class Paragraph implements ArticleElement {
    private final String htmlText;

    public Paragraph(String html) {
        this.htmlText = html;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public CharSequence getContent() {
        return linkify(trim(Html.fromHtml(htmlText)));
    }
}
