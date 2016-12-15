package fr.xgouchet.scparchive.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.ui.adapters.DrawerArticleAdapter;

/**
 * @author Xavier Gouchet
 */
public class DrawerArticlesFragment extends BaseListFragment<Article, DrawerArticleAdapter> {


    @Override public void itemSelected(@NonNull Article item) {
        Intent intent = new Intent();
        intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.getId());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override protected DrawerArticleAdapter getAdapter() {
        return new DrawerArticleAdapter(this);
    }
}
