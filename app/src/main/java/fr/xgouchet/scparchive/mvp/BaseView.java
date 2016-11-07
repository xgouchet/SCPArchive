package fr.xgouchet.scparchive.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public interface BaseView<P, T> {

    void setPresenter(@NonNull P presenter);

    void setError(@Nullable Throwable throwable);

    void setContent(@NonNull T content);

    void setLoading(boolean b);

}
