package fr.xgouchet.scparchive.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import fr.xgouchet.scparchive.ui.adapters.FavoriteArticleAdapter;

/**
 * @author Xavier Gouchet
 */
public class FavoriteArticlesFragment extends BaseListFragment<String, FavoriteArticleAdapter> {


    @Override public void itemSelected(@NonNull String item) {
        Intent intent = new Intent();
        intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override protected FavoriteArticleAdapter getAdapter() {
        return new FavoriteArticleAdapter(this);
    }
}
