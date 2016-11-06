package fr.xgouchet.scparchive.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import fr.xgouchet.scparchive.model.ArticleElement;

/**
 * @author Xavier Gouchet
 */
public class ArticleElementViewHolder extends BaseViewHolder<ArticleElement> {

    public ArticleElementViewHolder(@Nullable Listener<ArticleElement> listener, @NonNull View itemView) {
        super(listener, itemView);
    }

    @Override protected void onBindItem(@NonNull ArticleElement item) {

    }

}
