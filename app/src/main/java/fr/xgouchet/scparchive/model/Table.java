package fr.xgouchet.scparchive.model;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class Table implements ArticleElement {

    private final String[][] content;

    public Table(@NonNull String[][] content) {
        this.content = content;
    }
}
