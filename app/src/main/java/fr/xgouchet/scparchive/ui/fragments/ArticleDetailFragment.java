package fr.xgouchet.scparchive.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deezer.android.counsel.annotations.Trace;

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

@Trace
public class ArticleDetailFragment extends Fragment
        implements BaseView<ArticlePresenter, Article>, BaseViewHolder.Listener<ArticleElement> {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_STACK = "item_stack";

    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.empty) TextView empty;
    @BindView(R.id.stamp) View stamp;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;


    private String articleId;
    private ArticlePresenter presenter;
    private final BaseSimpleAdapter<ArticleElement, ? extends BaseViewHolder<ArticleElement>> adapter;
    @Nullable private Article content;

    public ArticleDetailFragment() {
        adapter = new ArticleElementAdapter(this);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.load(true);
            }
        });

        empty.setTypeface(((BaseActivity) getActivity()).getAppComponent().getTypefaceForCaption());
        return rootView;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article, menu);
        menu.findItem(R.id.toggle_favorite).setVisible(content != null);
        menu.findItem(R.id.share).setVisible(content != null);

        menu.findItem(R.id.navigate_prev).setVisible(presenter.canNavigateToPrevious());
        menu.findItem(R.id.navigate_next).setVisible(presenter.canNavigateToNext());
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
        list.setVisibility(View.GONE);
        Snackbar.make(list,
                "An error occured : " + (throwable == null ? "‽" : throwable.getMessage()),
                Snackbar.LENGTH_LONG)
                .show();
        getActivity().invalidateOptionsMenu();
        getActivity().setTitle("");
    }

    @Override public void setContent(@NonNull Article content) {
        final List<ArticleElement> elements = content.getElements();

        this.content = content;

        if (elements.size() == 0) {
            list.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
        adapter.update(elements);

        if (!TextUtils.equals(content.getId(), articleId)) {
            articleId = content.getId();
            list.scrollToPosition(0);
        }

        boolean favorite = presenter.isFavorite();
        stamp.setVisibility(favorite ? View.VISIBLE : View.GONE);

        getActivity().invalidateOptionsMenu();
        getActivity().setTitle(content.getTitle());

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
        swipeRefreshLayout.setRefreshing(active);

        if (active) {
            this.content = null;
            getActivity().setTitle("— Loading —");
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override public void onSelected(@NonNull ArticleElement item) {
        presenter.onInteraction(item);
    }


}
