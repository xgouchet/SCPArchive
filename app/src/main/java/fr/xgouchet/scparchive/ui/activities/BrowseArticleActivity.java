package fr.xgouchet.scparchive.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Drawer;
import fr.xgouchet.scparchive.ui.fragments.DrawerArticlesFragment;
import fr.xgouchet.scparchive.ui.presenters.DrawerArticlePresenter;
import fr.xgouchet.scparchive.ui.presenters.FavoriteArticlePresenter;

import static fr.xgouchet.scparchive.commons.SpannedUtils.withTypeface;

/**
 * @author Xavier Gouchet
 */
public class BrowseArticleActivity extends BaseFragmentActivity<Drawer, DrawerArticlesFragment> {

    public static final String ARG_DRAWER_ID = "drawer_id";
    public static final String ARG_DRAWER_NAME = "drawer_name";

    private DrawerArticlePresenter presenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new DrawerArticlePresenter(getAppComponent().getDrawerArticleRepository(), getItem());
        presenter.setView(getFragment());

        Typeface typeface = getAppComponent().getTypefaceForTitle();
        setTitle(withTypeface(getString(R.string.title_favorite_articles), typeface));
    }

    @Nullable @Override protected Drawer readItem(@Nullable Intent intent) {
        if (intent == null) return null;

        int id = intent.getIntExtra(ARG_DRAWER_ID, -1);
        if (id < 0) return null;

        String name = intent.getStringExtra(ARG_DRAWER_NAME);
        if (name == null) name = "";

        return new Drawer(name, id);
//        if (id < 10000) {
//            List<Article> articles = new ArrayList<>(100);
//            int end = id == 1 ? 99 : id + 100;
//            for (int i = id; i < end; ++i){
//                articles.add(new Article(Article.articleId(i), Article.articleTitle(i)));
//            }
//            return articles;
//        }
//
//        // TODO load from assets ?

    }

    @NonNull @Override
    protected DrawerArticlesFragment createFragment(@Nullable Drawer item) {
        return new DrawerArticlesFragment();
    }
}
