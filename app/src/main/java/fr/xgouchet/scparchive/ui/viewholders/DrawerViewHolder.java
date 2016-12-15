package fr.xgouchet.scparchive.ui.viewholders;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Drawer;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class DrawerViewHolder extends BaseViewHolder<Drawer> {

    @BindView(R.id.name) TextView name;

    public DrawerViewHolder(@Nullable BaseViewHolder.Listener<Drawer> listener,
                            @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);

        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForCaption();
        name.setTypeface(typeface);
    }

    @Override protected void onBindItem(@NonNull Drawer item) {
        name.setText(item.getName());
    }

    @OnClick(R.id.drawer)
    public void onClickFavorite() {
        fireSelected();
    }
}
