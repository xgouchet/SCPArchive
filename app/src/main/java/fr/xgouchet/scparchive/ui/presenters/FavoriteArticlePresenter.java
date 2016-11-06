package fr.xgouchet.scparchive.ui.presenters;

import android.support.annotation.NonNull;

import java.util.Comparator;

import fr.xgouchet.scparchive.mvp.BaseListPresenter;
import fr.xgouchet.scparchive.network.ArticleIdComparator;
import fr.xgouchet.scparchive.network.FavoriteArticleRepository;
import rx.Observable;

/**
 * @author Xavier Gouchet
 */
public class FavoriteArticlePresenter extends BaseListPresenter<String> {

    @NonNull private final ArticleIdComparator comparator;
    @NonNull private final FavoriteArticleRepository favoriteArticleRepository;

    public FavoriteArticlePresenter(@NonNull FavoriteArticleRepository favoriteArticleRepository) {
        this.favoriteArticleRepository = favoriteArticleRepository;
        comparator = new ArticleIdComparator();
    }

    @Override protected Observable<String> getItemsObservable() {
        return favoriteArticleRepository.fetchFavoriteArticles();
    }

    @Override protected Comparator<String> getComparator() {
        return comparator;
    }
}
