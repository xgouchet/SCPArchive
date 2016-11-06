package fr.xgouchet.scparchive.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.ui.fragments.FavoriteArticlesFragment;
import fr.xgouchet.scparchive.ui.presenters.FavoriteArticlePresenter;

import static fr.xgouchet.scparchive.commons.SpannedUtils.withTypeface;

/**
 * @author Xavier Gouchet
 */
public class SelectFavoriteArticleActivity extends BaseFragmentActivity<Void, FavoriteArticlesFragment> {

    private FavoriteArticlePresenter presenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new FavoriteArticlePresenter(getAppComponent().getFavoriteArticleRepository());
        presenter.setView(getFragment());

        Typeface typeface = getAppComponent().getTypefaceForTitle();
        setTitle(withTypeface(getString(R.string.title_favorite_articles), typeface));
    }

    @Nullable @Override protected Void readItem(@Nullable Intent intent) {
        return null;
    }

    @NonNull @Override protected FavoriteArticlesFragment createFragment(@Nullable Void item) {
        return new FavoriteArticlesFragment();
    }
}
