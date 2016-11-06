package fr.xgouchet.scparchive.ui.viewholders;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.ListItem;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class ListItemViewHolder extends ParagraphViewHolder {

    @BindView(R.id.bullet) TextView bullet;

    public ListItemViewHolder(@Nullable Listener<ArticleElement> listener,
                              @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);
        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForText();
        bullet.setTypeface(typeface);
    }

    @Override protected void onBindItem(@NonNull ArticleElement item) {
        super.onBindItem(item);
        if (!(item instanceof ListItem)) return;
        bullet.setText(((ListItem) item).getBullet());
    }
}
