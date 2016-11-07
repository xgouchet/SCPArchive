package fr.xgouchet.scparchive.model;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class FoldedElement implements ArticleElement {
    @NonNull private final ArticleElement element;
    private final int foldableId;

    public FoldedElement(@NonNull ArticleElement element, int foldableId) {
        this.element = element;
        this.foldableId = foldableId;
    }

    @NonNull public ArticleElement getElement() {
        return element;
    }

    public int getFoldableId() {
        return foldableId;
    }
}
