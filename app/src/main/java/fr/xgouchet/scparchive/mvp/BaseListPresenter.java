package fr.xgouchet.scparchive.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.deezer.android.counsel.annotations.Trace;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Xavier Gouchet
 */
@Trace
public abstract class BaseListPresenter<T> implements BasePresenter<BaseListView<T>, T> {

    @NonNull private final List<T> items;
    @NonNull private final CompositeSubscription subscriptions;

    @Nullable /*package*/ BaseListView<T> view;

    public BaseListPresenter() {
        items = new ArrayList<>();
        subscriptions = new CompositeSubscription();
    }

    @Override public void setView(@NonNull BaseListView<T> view) {
        this.view = view;
        view.setPresenter(this);
    }


    @Override public void load(boolean force) {
        if (view == null) return;

        if (!force) {
            view.setContent(items);
        }

        view.setLoading(true);

        items.clear();

        final Comparator<T> comparator = getComparator();
        final Observable<T> itemsObservable = getItemsObservable();


        // Sort if possible
        final Observable<List<T>> listObservable;
        if (comparator == null) {
            listObservable = itemsObservable.toList();
        } else {
            listObservable = itemsObservable.toSortedList(new Func2<T, T, Integer>() {
                @Override public Integer call(T t1, T t2) {
                    return comparator.compare(t1, t2);
                }
            });
        }

        Subscription subscription = listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<T>>() {
                    @Override public void onCompleted() {
                        view.setLoading(false);
                    }

                    @Override public void onError(Throwable e) {
                        view.setLoading(false);
                        view.setError(e);
                    }

                    @Override public void onNext(List<T> items) {
                        onItemsLoaded(items);
                    }
                });

        subscriptions.add(subscription);
    }

    @Nullable protected abstract Comparator<T> getComparator();

    @NonNull protected abstract Observable<T> getItemsObservable();

    /*package*/ void onItemsLoaded(List<T> items) {
        this.items.addAll(items);
        if (view == null) return;

        if (this.items.isEmpty()) {
            view.setEmpty();
        } else {
            view.setContent(this.items);
        }
    }

    public void itemSelected(@NonNull T item) {
        if (view == null) return;
        view.itemSelected(item);
    }


    @Override public void subscribe() {
        load(true);
    }

    @Override public void unsubscribe() {
        items.clear();
        subscriptions.clear();
    }
}
