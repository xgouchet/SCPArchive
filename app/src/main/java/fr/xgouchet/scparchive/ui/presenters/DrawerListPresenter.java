package fr.xgouchet.scparchive.ui.presenters;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

import fr.xgouchet.scparchive.model.Drawer;
import fr.xgouchet.scparchive.mvp.BaseListPresenter;
import fr.xgouchet.scparchive.network.DrawerComparator;
import rx.Observable;

/**
 * @author Xavier Gouchet
 */
public class DrawerListPresenter extends BaseListPresenter<Drawer> {



    @NonNull private final Comparator<Drawer> comparator;
    @NonNull private final List<Drawer> drawers;


    public DrawerListPresenter(@NonNull List<Drawer> drawers) {
        super();
        this.drawers = drawers;
        this.comparator = new DrawerComparator();
    }

    @NonNull @Override protected Observable<Drawer> getItemsObservable() {
        return Observable.from(drawers);
    }

    @NonNull @Override protected Comparator<Drawer> getComparator() {
        return comparator;
    }
}
