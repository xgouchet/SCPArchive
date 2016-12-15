package fr.xgouchet.scparchive.ui.viewholders;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class DrawerArticleViewHolder extends BaseViewHolder<Article> {

    @BindView(R.id.name) TextView name;

    public DrawerArticleViewHolder(@Nullable Listener<Article> listener,
                                   @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);

        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForCaption();
        name.setTypeface(typeface);
    }

    @Override protected void onBindItem(@NonNull Article item) {
        String id = item.getId();
        if (TextUtils.equals(id, "scp-2521")) {
            name.setText("●●/●●●●●/●●/●");
        } else {
            name.setText(item.getTitle());
        }
    }

    @OnClick(R.id.folder)
    public void onClickFavorite() {
        fireSelected();
    }
}
