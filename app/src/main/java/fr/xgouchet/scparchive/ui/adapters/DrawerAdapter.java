package fr.xgouchet.scparchive.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Drawer;
import fr.xgouchet.scparchive.ui.viewholders.BaseViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.DrawerViewHolder;

/**
 * @author Xavier Gouchet
 */
public class DrawerAdapter extends BaseSimpleAdapter<Drawer, DrawerViewHolder> {

    @NonNull private final BaseViewHolder.Listener<Drawer> listener;

    public DrawerAdapter(@NonNull BaseViewHolder.Listener<Drawer> listener) {
        this.listener = listener;
    }

    @Override protected DrawerViewHolder instantiateViewHolder(int viewType, View view) {
        return new DrawerViewHolder(listener, view);
    }

    @Override protected int getLayout(int viewType) {
        return R.layout.item_drawer;
    }
}
