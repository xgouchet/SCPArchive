package fr.xgouchet.scparchive.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.deezer.android.counsel.annotations.Trace;

import java.util.Collection;

import fr.xgouchet.scparchive.model.Article;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
@Trace
public class FavoriteArticleRepository {

    public static final String FAV_PREFIX = "fav_";


    @NonNull private final Context context;

    public FavoriteArticleRepository(@NonNull Context context) {
        this.context = context;
        setFavorite(new Article("scp-055", ""), false);
    }

    @NonNull private String getKey(@NonNull Article article) {
        return FAV_PREFIX + article.getId();
    }

    public boolean isFavorite(@NonNull Article article) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(getKey(article), false);
    }

    public void setFavorite(@NonNull Article article, boolean favorite) {
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        if (favorite) {
            editor.putBoolean(getKey(article), true);
        } else {
            editor.remove(getKey(article));
        }
        editor.apply();
    }

    @NonNull
    public Observable<String> fetchFavoriteArticles() {

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                final int prefixLength = FAV_PREFIX.length();
                final Collection<String> keys = prefs.getAll().keySet();
                for (String key : keys) {
                    if (key.startsWith(FAV_PREFIX)) {
                        String articleId = key.substring(prefixLength);
                        subscriber.onNext(articleId);
                    }
                }

                subscriber.onCompleted();
            }
        });
    }
}
