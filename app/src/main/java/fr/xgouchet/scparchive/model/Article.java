package fr.xgouchet.scparchive.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Xavier Gouchet
 */
public class Article {


    public static final int NOT_A_FOLDABLE = -1;


    public static String randomArticleId() {
        final int id = new Random().nextInt(3000);
        return articleId(id);
    }

    public static String articleId(int id) {
        return String.format(Locale.US, "scp-%03d", id);
    }

    @NonNull private final String articleId;
    @Nullable private String url;
    @NonNull private String title;
    private int foldableCount = 0;
    private int currentFoldableId = NOT_A_FOLDABLE;
    private boolean[] foldableState = new boolean[]{};

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
        appendElement(new Paragraph(html));
    }

    public void appendPhoto(String imgUrl, String caption) {
        appendElement(new Photo(imgUrl, caption));
    }

    public void appendHRule() {
        if (elements.isEmpty()) return;
        appendElement(new HRule());
    }

    public void appendImage(String imgUrl) {
        appendElement(new Image(imgUrl));
    }

    public void appendListItem(String html) {
        appendElement(new ListItem(html));
    }

    public void appendListItem(String html, String bullet) {
        appendElement(new ListItem(html, bullet));
    }

    public void appendBlockquote(String html) {
        appendElement(new Blockquote(html));
    }

    public void appendHeader(String html) {
        appendElement(new Header(html));
    }

    public void appendFooter(String html) {
        appendElement(new Footer(html));
    }

    public void appendFolded(String html) {
        if (currentFoldableId == NOT_A_FOLDABLE) {
            throw new IllegalStateException("You should call appendFolded after startFoldable");
        }

        final Link articleElement = new Link(html, currentFoldableId);
        elements.add(new FoldedElement(articleElement, currentFoldableId));
    }

    public void appendLink(String html) {
        appendElement(new Link(html, currentFoldableId));
    }

    public void appendTable(String[][] table) {
        appendElement(new Table(table));
    }

    public void appendElement(@NonNull ArticleElement e) {
        if (currentFoldableId == NOT_A_FOLDABLE) {
            elements.add(e);
        } else {
            elements.add(new UnfoldedElement(e, currentFoldableId));
        }
    }

    public boolean hasFoldables() {
        return foldableCount > 0;
    }


    public boolean isFolded(int foldableId) {
        return foldableState[foldableId];
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

    @Nullable public String getUrl() {
        return url;
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    public void startFoldable() {
        currentFoldableId = foldableCount++;
    }

    public void endFoldable() {
        currentFoldableId = NOT_A_FOLDABLE;
        foldableState = new boolean[foldableCount];
        Arrays.fill(foldableState, true);
    }

    public void toggleFolded(int foldableId) {
        foldableState[foldableId] = !foldableState[foldableId];
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
