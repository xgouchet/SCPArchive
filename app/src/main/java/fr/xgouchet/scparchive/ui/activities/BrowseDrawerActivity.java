package fr.xgouchet.scparchive.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import butterknife.BindView;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.ui.adapters.FilingCabinetAdapter;
import fr.xgouchet.scparchive.ui.fragments.ArticleDetailFragment;
import fr.xgouchet.scparchive.ui.fragments.DrawerListFragment;
import fr.xgouchet.scparchive.ui.presenters.DrawerListPresenter;
import android.support.v4.view.ViewPager;

import static butterknife.ButterKnife.bind;
import static fr.xgouchet.scparchive.commons.SpannedUtils.withTypeface;

/**
 * @author Xavier Gouchet
 */
public class BrowseDrawerActivity extends BaseActivity{

    public static final int REQUEST_BROWSE_ARTICLE = 77;

    private DrawerListPresenter presenter;
    @BindView(R.id.pager) ViewPager viewPager;
    private FilingCabinetAdapter pageAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        bind(this);

        pageAdapter = new FilingCabinetAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pageAdapter);

        Typeface typeface = getAppComponent().getTypefaceForTitle();
        setTitle(withTypeface(getString(R.string.title_favorite_articles), typeface));
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_BROWSE_ARTICLE) {
                String itemId = data.getStringExtra(ArticleDetailFragment.ARG_ITEM_ID);
                if (itemId != null) {
                    Intent intent = new Intent();
                    intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, itemId);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

}
