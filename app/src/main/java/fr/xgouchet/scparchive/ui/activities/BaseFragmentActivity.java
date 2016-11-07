package fr.xgouchet.scparchive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import fr.xgouchet.scparchive.R;

/**
 * @author Xavier Gouchet
 */
public abstract class BaseFragmentActivity<T, F extends Fragment> extends BaseActivity {

    private F fragment;

    private T item;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prepare content view
        setContentView(R.layout.activity_base);

        // get item from intent
        item = readItem(getIntent());

        if (savedInstanceState == null) {
            fragment = createFragment(item);

            if (fragment instanceof DialogFragment) {
                ((DialogFragment) fragment).show(getSupportFragmentManager(), "");
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        } else {
            fragment = (F) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        }
    }

    @Nullable protected abstract T readItem(@Nullable Intent intent);


    @NonNull protected abstract F createFragment(@Nullable T item);

    public T getItem() {
        return item;
    }

    public F getFragment() {
        return fragment;
    }
}


