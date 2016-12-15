package fr.xgouchet.scparchive.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deezer.android.counsel.annotations.Trace;

import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.model.Drawer;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
@Trace
public class DrawerArticleRepository {


    @NonNull private final Context context;

    public DrawerArticleRepository(@NonNull Context context) {
        this.context = context;
    }


    @NonNull
    public Observable<Article> fetchArticles(@NonNull final Drawer drawer) {

        return Observable.create(new Observable.OnSubscribe<Article>() {
            @Override public void call(Subscriber<? super Article> subscriber) {

                final int id = drawer.getIndex();
                if (id < 10000) {
                    List<Article> articles = new ArrayList<>(100);
                    int end = id == 1 ? 99 : id + 100;
                    for (int i = id; i < end; ++i) {
                        subscriber.onNext(new Article(Article.articleId(i), Article.articleTitle(i)));
                    }
                }
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//
//                final int prefixLength = FAV_PREFIX.length();
//                final Collection<String> keys = prefs.getAll().keySet();
//                for (String key : keys) {
//                    if (key.startsWith(FAV_PREFIX)) {
//                        String articleId = key.substring(prefixLength);
//                        subscriber.onNext(articleId);
//                    }
//                }

                subscriber.onCompleted();
            }
        });
    }
}
