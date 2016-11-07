package fr.xgouchet.scparchive.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.network.ArticleRepository;
import fr.xgouchet.scparchive.ui.fragments.ArticleDetailFragment;
import fr.xgouchet.scparchive.ui.presenters.ArticlePresenter;

import static fr.xgouchet.scparchive.commons.SpannedUtils.withTypeface;

public class ArticleDetailActivity extends BaseFragmentActivity<String, ArticleDetailFragment> {


    public static final int FAVORITE_REQUEST = 55;
    private ArticlePresenter presenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");

        presenter = new ArticlePresenter(this,
                getAppComponent().getArticleRepository(),
                getAppComponent().getFavoriteArticleRepository());

        if (savedInstanceState == null) {
            presenter.setArticleId(getItem());
        } else {
            presenter.setArticleId(savedInstanceState.getString(ArticleDetailFragment.ARG_ITEM_ID));
        }
        presenter.setView(getFragment());
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ArticleDetailFragment.ARG_ITEM_ID, presenter.getArticleId());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.random:
                presenter.goToRandom();
                break;
            case R.id.goTo:
                promptNumber();
                break;
            case R.id.goToSelected:
                promptSelected();
                break;
            case R.id.about:
                presenter.goToArticle(ArticleRepository.ABOUT_ARTICLE);
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FAVORITE_REQUEST) {
                String itemId = data.getStringExtra(ArticleDetailFragment.ARG_ITEM_ID);
                if (itemId != null) {
                    presenter.goToArticle(itemId);
                }
            }
        }
    }

    private void promptSelected() {
        Intent intent = new Intent(this, SelectFavoriteArticleActivity.class);
        startActivityForResult(intent, FAVORITE_REQUEST);
    }

    @NonNull @Override protected String readItem(@Nullable Intent intent) {
        String articleId = Article.randomArticleId();

        // PARSE URL
        if (intent != null) {
            Uri url = intent.getData();
            if (url != null) {

                articleId = url.getPath();

                // remove leading slash
                while (articleId.charAt(0) == '/') {
                    articleId = articleId.substring(1);
                }
            }
        }

        return articleId;
    }

    @NonNull @Override protected ArticleDetailFragment createFragment(@Nullable String articleId) {
        Bundle arguments = new Bundle();
        arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, articleId);

        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override public void setTitle(CharSequence title) {
        Typeface typeface = getAppComponent().getTypefaceForTitle();
        super.setTitle(withTypeface(title, typeface));
    }

    private void promptNumber() {
        final EditText input = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt_go_to);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int articleId;
                try {
                    articleId = Integer.parseInt(input.getText().toString());
                    presenter.goToArticle(articleId);
                } catch (NumberFormatException e) {
                    getFragment().setError(e);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
