package fr.xgouchet.scparchive.mvp;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author Xavier Gouchet
 */
public interface BaseListView<T> extends BaseView<BaseListPresenter<T>, List<T>> {

    void setLoading(boolean active);

    void setEmpty();

    void itemSelected(@NonNull T item);
}
