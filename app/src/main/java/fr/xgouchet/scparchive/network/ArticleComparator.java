package fr.xgouchet.scparchive.network;

import android.support.annotation.NonNull;

import java.util.Comparator;

import fr.xgouchet.scparchive.model.Article;

/**
 * @author Xavier Gouchet
 */
public class ArticleComparator implements Comparator<Article> {

    @NonNull private final Comparator<String> idComparator;

    public ArticleComparator(@NonNull Comparator<String> idComparator) {
        this.idComparator = idComparator;
    }

    @Override public int compare(Article a1, Article a2) {
        return idComparator.compare(a1.getId(), a2.getId());
    }


}
