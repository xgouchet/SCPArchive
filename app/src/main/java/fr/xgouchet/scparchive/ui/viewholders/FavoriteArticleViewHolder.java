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
import fr.xgouchet.scparchive.inject.components.AppComponent;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class FavoriteArticleViewHolder extends BaseViewHolder<String> {

    @BindView(R.id.name) TextView name;

    public FavoriteArticleViewHolder(@Nullable Listener<String> listener,
                                     @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);

        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForCaption();
        name.setTypeface(typeface);
    }

    @Override protected void onBindItem(@NonNull String item) {
        Typeface typeface;
        if (item.equals("scp-095-j")) {
            typeface = getAppComponent(itemView).getTypefaceForSCP095J();
        } else {
            typeface = getAppComponent(itemView).getTypefaceForCaption();
        }
        name.setTypeface(typeface);

        if (TextUtils.equals(item, "scp-2521")) {
            name.setText("●●/●●●●●/●●/●");
        } else {
            name.setText(item.toUpperCase());
        }
    }

    @OnClick(R.id.favorite)
    public void onClickFavorite() {
        fireSelected();
    }

    private AppComponent getAppComponent(@NonNull View itemView) {
        return BaseApplication.from(itemView.getContext())
                .getAppComponent();
    }
}
