package fr.xgouchet.scparchive.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.ui.viewholders.BaseViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.FavoriteArticleViewHolder;

/**
 * @author Xavier Gouchet
 */
public class FavoriteArticleAdapter extends BaseSimpleAdapter<String, FavoriteArticleViewHolder> {

    @NonNull private final BaseViewHolder.Listener<String> listener;

    public FavoriteArticleAdapter(@NonNull BaseViewHolder.Listener<String> listener) {
        this.listener = listener;
    }

    @Override protected FavoriteArticleViewHolder instantiateViewHolder(int viewType, View view) {
        return new FavoriteArticleViewHolder(listener, view);
    }

    @Override protected int getLayout(int viewType) {
        return R.layout.item_favorite;
    }
}
