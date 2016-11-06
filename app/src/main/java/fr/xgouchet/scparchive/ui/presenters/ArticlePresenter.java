package fr.xgouchet.scparchive.ui.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.mvp.BasePresenter;
import fr.xgouchet.scparchive.mvp.BaseView;
import fr.xgouchet.scparchive.network.ArticleRepository;
import fr.xgouchet.scparchive.network.FavoriteArticleRepository;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Xavier Gouchet
 */
public class ArticlePresenter
        implements BasePresenter<BaseView<? super ArticlePresenter, Article>, Article> {

    @Nullable private BaseView<? super ArticlePresenter, Article> view;
    @Nullable private Article article;
    private String articleId;

    @NonNull private final CompositeSubscription subscriptions = new CompositeSubscription();
    @NonNull private final ArticleRepository articleRepository;
    @NonNull private final FavoriteArticleRepository favoriteArticleRepository;

    public ArticlePresenter(@NonNull ArticleRepository repository,
                            @NonNull FavoriteArticleRepository favoriteArticleRepository) {
        this.articleRepository = repository;
        this.favoriteArticleRepository = favoriteArticleRepository;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override public void setView(@NonNull BaseView<? super ArticlePresenter, Article> view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override public void subscribe() {
        load(false);
    }

    @Override public void unsubscribe() {
        article = null;
        subscriptions.clear();
    }

    public void goToRandom() {
        goToArticle(Article.randomArticleId());
    }

    public void goToArticle(int articleId) {
        goToArticle(Article.articleId(articleId));
    }

    public void goToArticle(String randomId) {
        setArticleId(randomId);
        load(false);
    }

    @Override public void load(boolean force) {
        if (view == null) return;

        if (article != null) {
            force |= (!TextUtils.equals(articleId, article.getId()));
            if ((!force)) {
                view.setContent(article);
                return;
            }
        }

        view.setLoading(true);

        article = null;

        Subscription subscription = articleRepository.fetchArticle(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override public void onCompleted() {
                        view.setLoading(false);
                    }

                    @Override public void onError(Throwable e) {
                        view.setLoading(false);
                        view.setError(e);
                        Log.e("ArticlePresenter", "Error loading", e);
                    }

                    @Override public void onNext(Article article) {
                        onArticleLoaded(article);
                    }
                });

        subscriptions.add(subscription);
    }

    public void toggleFavorite() {
        if (article == null) return;
        boolean fav = favoriteArticleRepository.isFavorite(article);
        favoriteArticleRepository.setFavorite(article, !fav);

        if (view == null) return;
        view.setContent(article);
    }

    public boolean isFavorite() {
        if (article == null) return false;
        return favoriteArticleRepository.isFavorite(article);
    }

    /**/ void onArticleLoaded(@Nullable Article article) {
        this.article = article;
        if (view == null) return;

        if (article == null) {
            view.setError(new RuntimeException("Nothing to display :-("));
        } else {
            view.setContent(article);
        }
    }
}
