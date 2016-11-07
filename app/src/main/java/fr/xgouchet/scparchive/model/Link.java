package fr.xgouchet.scparchive.model;

/**
 * @author Xavier Gouchet
 */
public class Link extends Paragraph {

    private final int foldableId;

    public Link(String html, int foldableId) {
        super(html);
        this.foldableId = foldableId;
    }

    public int getFoldableId() {
        return foldableId;
    }
}
