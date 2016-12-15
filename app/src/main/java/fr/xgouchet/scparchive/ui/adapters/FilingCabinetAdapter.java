package fr.xgouchet.scparchive.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

import fr.xgouchet.scparchive.model.Drawer;
import fr.xgouchet.scparchive.ui.activities.BaseActivity;
import fr.xgouchet.scparchive.ui.fragments.DrawerListFragment;
import fr.xgouchet.scparchive.ui.presenters.DrawerListPresenter;

import static java.util.Arrays.asList;

/**
 * @author Xavier Gouchet
 */
public class FilingCabinetAdapter extends FragmentStatePagerAdapter {


    public static final List<Drawer> SERIES_1 = asList(new Drawer("SCP 001 - 099", 1),
            new Drawer("SCP 100 - 199", 100),
            new Drawer("SCP 200 - 299", 200),
            new Drawer("SCP 300 - 399", 300),
            new Drawer("SCP 400 - 499", 400),
            new Drawer("SCP 500 - 599", 500),
            new Drawer("SCP 600 - 699", 600),
            new Drawer("SCP 700 - 799", 700),
            new Drawer("SCP 800 - 899", 800),
            new Drawer("SCP 900 - 999", 900));
    public static final List<Drawer> SERIES_2 = asList(new Drawer("SCP 1000 - 1099", 1000),
            new Drawer("SCP 1100 - 1199", 1100),
            new Drawer("SCP 1200 - 1299", 1200),
            new Drawer("SCP 1300 - 1399", 1300),
            new Drawer("SCP 1400 - 1499", 1400),
            new Drawer("SCP 1500 - 1599", 1500),
            new Drawer("SCP 1600 - 1699", 1600),
            new Drawer("SCP 1700 - 1799", 1700),
            new Drawer("SCP 1800 - 1899", 1800),
            new Drawer("SCP 1900 - 1999", 1900));
    public static final List<Drawer> SERIES_3 = asList(new Drawer("SCP 2000 - 2099", 2000),
            new Drawer("SCP 2100 - 2199", 2100),
            new Drawer("SCP 2200 - 2299", 2200),
            new Drawer("SCP 2300 - 2399", 2300),
            new Drawer("SCP 2400 - 2499", 2400),
            new Drawer("SCP 2500 - 2599", 2500),
            new Drawer("SCP 2600 - 2699", 2600),
            new Drawer("SCP 2700 - 2799", 2700),
            new Drawer("SCP 2800 - 2899", 2800),
            new Drawer("SCP 2900 - 2999", 2900));
    public static final List<Drawer> SERIES_OTHER = asList(new Drawer("SCP Jokes", 100000),
            new Drawer("SCP Archived", 110000),
            new Drawer("SCP Explained", 120000),
            new Drawer("Logs", 130000),
            new Drawer("", 140000),
            new Drawer("", 150000),
            new Drawer("", 160000),
            new Drawer("", 170000),
            new Drawer("", 180000),
            new Drawer("", 190000));

    public static final int PAGE_SERIES_1 = 0;
    public static final int PAGE_SERIES_2 = 1;
    public static final int PAGE_SERIES_3 = 2;
    public static final int PAGE_SERIES_OTHER = 3;

    public static final int PAGE_COUNT = 4;

    private final WeakReference<BaseActivity> activityRef;

    public FilingCabinetAdapter(@NonNull FragmentManager fm,
                                @NonNull BaseActivity activity) {
        super(fm);

        activityRef = new WeakReference<>(activity);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    public Fragment getItem(int position) {
        BaseActivity activity = activityRef.get();
        if (activity == null) {
            return null;
        }

        switch (position) {
            case PAGE_SERIES_1: {
                final DrawerListFragment fragment = new DrawerListFragment();
                DrawerListPresenter presenter = new DrawerListPresenter(SERIES_1);
                presenter.setView(fragment);
                return fragment;
            }
            case PAGE_SERIES_2: {
                final DrawerListFragment fragment = new DrawerListFragment();
                DrawerListPresenter presenter = new DrawerListPresenter(SERIES_2);
                presenter.setView(fragment);
                return fragment;
            }
            case PAGE_SERIES_3: {
                final DrawerListFragment fragment = new DrawerListFragment();
                DrawerListPresenter presenter = new DrawerListPresenter(SERIES_3);
                presenter.setView(fragment);
                return fragment;
            }
            case PAGE_SERIES_OTHER: {
                final DrawerListFragment fragment = new DrawerListFragment();
                DrawerListPresenter presenter = new DrawerListPresenter(SERIES_OTHER);
                presenter.setView(fragment);
                return fragment;
            }

            default:
                return null;
        }
    }

    @Override public CharSequence getPageTitle(int position) {
        switch (position) {
            case PAGE_SERIES_1:
                return "SCP Series I";
            case PAGE_SERIES_2:
                return "SCP Series II";
            case PAGE_SERIES_3:
                return "SCP Series III";
            case PAGE_SERIES_OTHER:
                return "Other";
        }


        return super.getPageTitle(position);
    }
}
