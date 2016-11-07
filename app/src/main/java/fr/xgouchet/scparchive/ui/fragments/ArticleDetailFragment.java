package fr.xgouchet.scparchive.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import fr.xgouchet.scparchive.BuildConfig;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.mvp.BaseView;
import fr.xgouchet.scparchive.ui.activities.BaseActivity;
import fr.xgouchet.scparchive.ui.adapters.ArticleElementAdapter;
import fr.xgouchet.scparchive.ui.adapters.BaseSimpleAdapter;
import fr.xgouchet.scparchive.ui.presenters.ArticlePresenter;
import fr.xgouchet.scparchive.ui.viewholders.BaseViewHolder;

import static butterknife.ButterKnife.bind;

public class ArticleDetailFragment extends Fragment
        implements BaseView<ArticlePresenter, Article> {

    public static final String ARG_ITEM_ID = "item_id";

    @BindView(R.id.loading) ContentLoadingProgressBar loadingProgressBar;
    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.empty) TextView empty;
    @BindView(R.id.stamp) View stamp;


    private String articleId;
    private ArticlePresenter presenter;
    private final BaseSimpleAdapter<ArticleElement, ? extends BaseViewHolder<ArticleElement>> adapter;

    public ArticleDetailFragment() {
        adapter = new ArticleElementAdapter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            articleId = getArguments().getString(ARG_ITEM_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        bind(this, rootView);

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);

        empty.setTypeface(((BaseActivity) getActivity()).getAppComponent().getTypefaceForCaption());
        return rootView;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.toggle_favorite:
                presenter.toggleFavorite();
                break;
            case R.id.share:
                presenter.shareArticle();
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }


    @Override public void onResume() {
        super.onResume();

        if (presenter != null) presenter.subscribe();
    }

    @Override public void onPause() {
        super.onPause();

        if (presenter != null) presenter.unsubscribe();
    }

    @Override public void setPresenter(@NonNull ArticlePresenter presenter) {
        this.presenter = presenter;
    }

    @Override public void setError(@Nullable Throwable throwable) {
        Snackbar.make(list,
                "An error occured : " + (throwable == null ? "‽" : throwable.getMessage()),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override public void setContent(@NonNull Article content) {
        getActivity().invalidateOptionsMenu();
        getActivity().setTitle(content.getTitle());
        final List<ArticleElement> elements = content.getElements();

        if (elements.size() == 0) {
            list.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
        adapter.update(elements);

        if (content.getId() != articleId) {
            articleId = content.getId();
            list.scrollToPosition(0);
        }

        boolean favorite = presenter.isFavorite();
        stamp.setVisibility(favorite ? View.VISIBLE : View.GONE);

        if (BuildConfig.DEBUG) {
            final String[] unhandledTags = content.getUnhandledTags();
            if (unhandledTags.length > 0) {
                Snackbar.make(list,
                        "Unhandled tags : " + Arrays.toString(unhandledTags),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override public void setLoading(boolean active) {
        loadingProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

}
