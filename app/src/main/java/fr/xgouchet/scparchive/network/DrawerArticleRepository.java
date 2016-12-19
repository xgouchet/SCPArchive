package fr.xgouchet.scparchive.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.JsonReader;

import com.deezer.android.counsel.annotations.Trace;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.model.Drawer;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
@Trace
public class DrawerArticleRepository {

    public static final int ID_JOKES = 100000;
    public static final int ID_ARCHIVED = 110000;
    public static final int ID_EXPLAINED = 120000;
    public static final int ID_LOGS = 130000;
    public static final int ID_EMPTY = 200000;

    public static final int MAX_STANDARD_ID = 100000;


    @NonNull private final Context context;

    public DrawerArticleRepository(@NonNull Context context) {
        this.context = context;
    }


    @NonNull
    public Observable<Article> fetchArticles(@NonNull final Drawer drawer) {

        return Observable.create(new Observable.OnSubscribe<Article>() {
            @Override public void call(Subscriber<? super Article> subscriber) {

                final int id = drawer.getIndex();
                if (id < MAX_STANDARD_ID) {
                    List<Article> articles = new ArrayList<>(100);
                    int end = id == 1 ? 99 : id + 100;
                    for (int i = id; i < end; ++i) {
                        subscriber.onNext(new Article(Article.articleId(i), Article.articleTitle(i)));
                    }
                    subscriber.onCompleted();
                } else {
                    try {
                        loadFromRes(id, subscriber);
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    private void loadFromRes(int id, Subscriber<? super Article> subscriber) throws IOException {
        int res = getResourceId(id);

        if (res == 0) return;

        InputStream in = context.getResources().openRawResource(res);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        reader.beginArray();
        while (reader.hasNext()) {
            Article article = readArticle(reader);
            if (article != null) {
                subscriber.onNext(article);
            }
        }
        reader.endArray();

    }

    private Article readArticle(JsonReader reader) throws IOException {
        String path = null;
        String id = null;
        String title = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextString();
            } else if (name.equals("path")) {
                path = reader.nextString();
            } else if (name.equals("title")) {
                title = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (path == null) return null;
        if (id == null) return null;
        return new Article(path, id);
    }

    private int getResourceId(int id) {
        switch (id) {
            case ID_JOKES:
                return R.raw.jokes;
            case ID_ARCHIVED:
                return R.raw.archived;
            case ID_EXPLAINED:
                return R.raw.explained;
            case ID_LOGS:
                return R.raw.logs;
        }
        return 0;
    }
}
