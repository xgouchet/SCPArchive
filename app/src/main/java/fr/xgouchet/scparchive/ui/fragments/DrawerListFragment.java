package fr.xgouchet.scparchive.ui.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;

import fr.xgouchet.scparchive.model.Drawer;
import fr.xgouchet.scparchive.ui.activities.BrowseArticleActivity;
import fr.xgouchet.scparchive.ui.activities.BrowseDrawerActivity;
import fr.xgouchet.scparchive.ui.adapters.DrawerAdapter;

/**
 * @author Xavier Gouchet
 */
public class DrawerListFragment extends BaseListFragment<Drawer, DrawerAdapter> {



    @Override public void itemSelected(@NonNull Drawer item) {
        Intent intent = new Intent(getActivity(), BrowseArticleActivity.class);

        intent.putExtra(BrowseArticleActivity.ARG_DRAWER_ID, item.getIndex());
        getActivity().startActivityForResult(intent, BrowseDrawerActivity.REQUEST_BROWSE_ARTICLE);
    }

    @Override protected DrawerAdapter getAdapter() {
        return new DrawerAdapter(this);
    }


}

