package fr.xgouchet.scparchive.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.ui.viewholders.BaseViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.DrawerArticleViewHolder;

/**
 * @author Xavier Gouchet
 */
public class DrawerArticleAdapter extends BaseSimpleAdapter<Article, DrawerArticleViewHolder> {

    @NonNull private final BaseViewHolder.Listener<Article> listener;

    public DrawerArticleAdapter(@NonNull BaseViewHolder.Listener<Article> listener) {
        this.listener = listener;
    }

    @Override protected DrawerArticleViewHolder instantiateViewHolder(int viewType, View view) {
        return new DrawerArticleViewHolder(listener, view);
    }

    @Override protected int getLayout(int viewType) {
        return R.layout.item_article_folder;
    }
}
