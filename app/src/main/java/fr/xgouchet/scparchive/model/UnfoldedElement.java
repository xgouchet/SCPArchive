package fr.xgouchet.scparchive.model;

/**
 * @author Xavier Gouchet
 */
public class UnfoldedElement implements ArticleElement {

    private final ArticleElement element;
    private final int foldableId;

    public UnfoldedElement(ArticleElement element, int foldableId) {
        this.element = element;
        this.foldableId = foldableId;
    }

    public ArticleElement getElement() {
        return element;
    }

    public int getFoldableId() {
        return foldableId;
    }
}
