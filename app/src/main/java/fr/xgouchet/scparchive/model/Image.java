package fr.xgouchet.scparchive.model;

/**
 * @author Xavier Gouchet
 */
public class Image implements ArticleElement {

    private final String url;

    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
