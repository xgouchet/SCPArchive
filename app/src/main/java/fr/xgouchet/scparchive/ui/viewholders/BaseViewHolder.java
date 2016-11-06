package fr.xgouchet.scparchive.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Xavier Gouchet
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public interface Listener<T> {

        void onSelected(@NonNull T item);

    }

    @Nullable private final Listener<T> listener;

    @Nullable private T item;

    protected BaseViewHolder(@Nullable Listener<T> listener, @NonNull View itemView) {
        super(itemView);
        this.listener = listener;
    }


    public final void bindItem(@NonNull T item) {
        this.item = item;
        onBindItem(item);
    }

    protected void fireSelected() {
        if ((item != null) && (listener != null)) {
            listener.onSelected(item);
        }
    }

    protected abstract void onBindItem(@NonNull T item);
}
