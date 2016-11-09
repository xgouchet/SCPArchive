package fr.xgouchet.scparchive.ui.viewholders;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.ArticleElement;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class LinkViewHolder extends ParagraphViewHolder {

    public LinkViewHolder(@Nullable Listener<ArticleElement> listener, @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);


        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForCaption();
        content.setTypeface(typeface);
    }

    @OnClick(R.id.content) public void onClick() {
        fireSelected();
    }
}
