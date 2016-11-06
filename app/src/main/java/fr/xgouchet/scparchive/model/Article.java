package fr.xgouchet.scparchive.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Xavier Gouchet
 */
public class Article {

    public static String randomArticleId() {
        final int id = new Random().nextInt(3000);
        return articleId(id);
    }

    public static String articleId(int id) {
        return String.format(Locale.US, "scp-%03d", id);
    }

    @NonNull private final String articleId;
    @NonNull private String title;

    @NonNull private final List<ArticleElement> elements = new ArrayList<>();
    @NonNull private final List<String> unhandledTags = new ArrayList<>();

    public Article(@NonNull String articleId, @NonNull String title) {
        this.articleId = articleId;
        this.title = title;
    }

    @NonNull public String getId() {
        return articleId;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull public String getTitle() {
        return title;
    }

    public void appendParagraph(@NonNull String html) {
        elements.add(new Paragraph(html));
    }

    public void appendPhoto(String imgUrl, String caption) {
        elements.add(new Photo(imgUrl, caption));
    }

    public void appendHRule() {
        if (elements.isEmpty()) return;
        elements.add(new HRule());
    }

    public void appendImage(String imgUrl) {
        elements.add(new Image(imgUrl));
    }

    public void appendListItem(String html) {
        elements.add(new ListItem(html));
    }

    public void appendListItem(String html, String bullet) {
        elements.add(new ListItem(html, bullet));
    }

    public void appendBlockquote(String html) {
        elements.add(new Blockquote(html));
    }

    @NonNull public List<ArticleElement> getElements() {
        return elements;
    }

    public void addUnhandledTag(String tagName) {
        if (!unhandledTags.contains(tagName)) {
            unhandledTags.add(tagName);
        }
    }


    @NonNull public String[] getUnhandledTags() {
        return unhandledTags.toArray(new String[unhandledTags.size()]);
    }
}
