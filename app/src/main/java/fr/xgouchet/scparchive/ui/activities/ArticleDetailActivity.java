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
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.deezer.android.counsel.annotations.Trace;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.network.ArticleRepository;
import fr.xgouchet.scparchive.ui.fragments.ArticleDetailFragment;
import fr.xgouchet.scparchive.ui.presenters.ArticlePresenter;

import static fr.xgouchet.scparchive.commons.SpannedUtils.withTypeface;

@Trace
public class ArticleDetailActivity extends BaseFragmentActivity<String, ArticleDetailFragment> {


    public static final int FAVORITE_REQUEST = 55;
    public static final int BROWSE_REQUEST = 66;

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
            presenter.setArticleStack(savedInstanceState.getStringArrayList(ArticleDetailFragment.ARG_ITEM_STACK));
        }
        presenter.setView(getFragment());
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ArticleDetailFragment.ARG_ITEM_ID, presenter.getArticleId());
        outState.putStringArrayList(ArticleDetailFragment.ARG_ITEM_STACK, presenter.getArticleStack());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.navigate_prev:
                presenter.goToPrevious();
                break;
            case R.id.navigate_next:
                presenter.goToNext();
                break;
            case R.id.random:
                presenter.goToRandom();
                break;
            case R.id.goTo:
                promptNumber();
                break;
            case R.id.goToBrowser:
                promptBrowser();
                break;
            case R.id.goToFavorite:
                promptFavorite();
                break;
            case R.id.about:
                presenter.goToArticle(ArticleRepository.ABOUT_ARTICLE, true);
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String articleId = readItem(intent);
        if (TextUtils.isEmpty(articleId)) return;
        presenter.goToArticle(articleId, true);
    }

    @Override public void onBackPressed() {
        if (presenter.onBackpressed()) return;
        super.onBackPressed();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if ((requestCode == FAVORITE_REQUEST) || (requestCode == BROWSE_REQUEST)) {
                String itemId = data.getStringExtra(ArticleDetailFragment.ARG_ITEM_ID);
                if (itemId != null) {
                    presenter.goToArticle(itemId, true);
                }
            }
        }
    }

    private void promptFavorite() {
        Intent intent = new Intent(this, SelectFavoriteArticleActivity.class);
        startActivityForResult(intent, FAVORITE_REQUEST);
    }

    private void promptBrowser() {
        Intent intent = new Intent(this, BrowseDrawerActivity.class);
        startActivityForResult(intent, BROWSE_REQUEST);
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

    @Override public void setTitle(@NonNull CharSequence title) {
        Typeface typeface;
        if (title.equals("SCP-095-J")) {
            typeface = getAppComponent().getTypefaceForSCP095J();
        } else {
            typeface = getAppComponent().getTypefaceForTitle();
        }
        super.setTitle(withTypeface(title, typeface));
    }

    private void promptNumber() {
        final EditText input = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt_go_to);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.requestFocus();
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
