package fr.xgouchet.scparchive.model;

/**
 * @author Xavier Gouchet
 */
public class Header extends Paragraph {
    private final int level;

    public Header(String html, int level) {
        super(html);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
