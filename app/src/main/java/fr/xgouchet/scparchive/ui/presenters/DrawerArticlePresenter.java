package fr.xgouchet.scparchive.ui.presenters;

import android.support.annotation.NonNull;

import java.util.Comparator;

import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.model.Drawer;
import fr.xgouchet.scparchive.mvp.BaseListPresenter;
import fr.xgouchet.scparchive.network.ArticleComparator;
import fr.xgouchet.scparchive.network.ArticleIdComparator;
import fr.xgouchet.scparchive.network.DrawerArticleRepository;
import rx.Observable;

/**
 * @author Xavier Gouchet
 */
public class DrawerArticlePresenter extends BaseListPresenter<Article> {

    @NonNull private final Comparator<Article> comparator;
    @NonNull private final DrawerArticleRepository repository;
    @NonNull private final Drawer drawer;

    public DrawerArticlePresenter(@NonNull DrawerArticleRepository repository,
                                  @NonNull Drawer drawer) {
        this.repository = repository;
        this.drawer = drawer;
        comparator = new ArticleComparator(new ArticleIdComparator());
    }

    @NonNull @Override protected Observable<Article> getItemsObservable() {
        return repository.fetchArticles(drawer);
    }

    @NonNull @Override protected Comparator<Article> getComparator() {
        return comparator;
    }
}
