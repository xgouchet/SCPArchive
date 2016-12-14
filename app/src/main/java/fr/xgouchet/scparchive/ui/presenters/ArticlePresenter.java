package fr.xgouchet.scparchive.ui.presenters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.Link;
import fr.xgouchet.scparchive.mvp.BasePresenter;
import fr.xgouchet.scparchive.mvp.BaseView;
import fr.xgouchet.scparchive.network.ArticleRepository;
import fr.xgouchet.scparchive.network.FavoriteArticleRepository;
import fr.xgouchet.scparchive.network.ResolveFoldableArticle;
import rx.Observable;
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
    @NonNull private final List<String> articleStack = new LinkedList<>();
    private String articleId;

    @NonNull private final CompositeSubscription subscriptions = new CompositeSubscription();
    @NonNull private final ArticleRepository articleRepository;
    @NonNull private final FavoriteArticleRepository favoriteArticleRepository;
    @NonNull private final Context context;

    public ArticlePresenter(@NonNull Context context,
                            @NonNull ArticleRepository repository,
                            @NonNull FavoriteArticleRepository favoriteArticleRepository) {
        this.context = context;
        this.articleRepository = repository;
        this.favoriteArticleRepository = favoriteArticleRepository;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setArticleStack(@Nullable List<String> stack) {
        articleStack.clear();
        if (stack != null) articleStack.addAll(stack);
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
        goToArticle(Article.randomArticleId(), true);
    }


    public void goToPrevious() {
        int id = Article.getId(articleId);
        if (id > 0) {
            goToArticle(id - 1);
        }
    }

    public void goToNext() {
        int id = Article.getId(articleId);
        if (id >= 0) {
            goToArticle(id + 1);
        }
    }

    public void goToArticle(int articleId) {
        goToArticle(Article.articleId(articleId), true);
    }

    public void goToArticle(String articleId, boolean addToStack) {
        if (TextUtils.equals(articleId, this.articleId)) {
            load(false);
        } else {
            if (addToStack) articleStack.add(0, this.articleId);
            this.articleId = articleId;
            load(true);
        }
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
        subscriptions.clear();
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
        //noinspection SimplifiableIfStatement
        if (article == null) return false;
        return favoriteArticleRepository.isFavorite(article);
    }

    public void shareArticle() {
        if (article == null) return;
        if (view == null) return;
        final String url = article.getUrl();
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, url);
            intent.setType("text/plain");
            intent.setClipData(ClipData.newRawUri("uri", Uri.parse(url)));

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.prompt_share_with)));
        }
    }

    public String getArticleId() {
        return articleId;
    }

    @NonNull public ArrayList<String> getArticleStack() {
        return new ArrayList<>(articleStack);
    }

    public boolean onBackpressed() {
        if (articleStack.isEmpty()) return false;

        String lastId = articleStack.remove(0);
        goToArticle(lastId, false);
        return true;
    }

    public void onInteraction(@NonNull ArticleElement item) {
        if (article == null) return;
        if (item instanceof Link) {
            article.toggleFolded(((Link) item).getFoldableId());
            resolveFoldables();
        }
    }

    public boolean canNavigateToPrevious() {
        int id = Article.getId(articleId);
        return id > 1;
    }

    public boolean canNavigateToNext() {
        int id = Article.getId(articleId);
        return id >= 0;
    }


    private void onArticleLoaded(@Nullable Article article) {
        this.article = article;
        if (view == null) return;

        if (article == null) {
            view.setError(new RuntimeException("Nothing to display :-("));
        } else {
            if (article.hasFoldables()) {
                resolveFoldables();
            } else {
                view.setContent(article);
            }
        }
    }

    private void onArticleResolved(@Nullable Article article) {
        if (view == null) return;
        if (article == null) return;
        view.setContent(article);
    }

    private void resolveFoldables() {
        if (view == null) return;
        subscriptions.clear();

        Subscription subscription = Observable.just(this.article)
                .map(new ResolveFoldableArticle())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override public void onCompleted() {
                        view.setLoading(false);
                    }

                    @Override public void onError(Throwable e) {
                        view.setLoading(false);
                        view.setError(e);
                        Log.e("ArticlePresenter", "Error resolving foldables", e);
                    }

                    @Override public void onNext(Article article) {
                        onArticleResolved(article);
                    }
                });

        subscriptions.add(subscription);
    }
}
