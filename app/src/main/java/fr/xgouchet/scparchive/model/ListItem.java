package fr.xgouchet.scparchive.model;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class ListItem extends Paragraph {

    private final String bullet;

    public ListItem(@NonNull String html) {
        this(html, "-");
    }

    public ListItem(@NonNull String html, @NonNull String bullet) {
        super(html);
        this.bullet = bullet;
    }

    public String getBullet() {
        return bullet;
    }
}
