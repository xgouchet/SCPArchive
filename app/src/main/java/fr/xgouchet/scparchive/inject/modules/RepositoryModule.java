package fr.xgouchet.scparchive.inject.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fr.xgouchet.scparchive.inject.annotations.ApplicationContext;
import fr.xgouchet.scparchive.inject.annotations.ApplicationScope;
import fr.xgouchet.scparchive.network.ArticleRepository;
import fr.xgouchet.scparchive.network.DrawerArticleRepository;
import fr.xgouchet.scparchive.network.FavoriteArticleRepository;
import okhttp3.OkHttpClient;

/**
 * @author Xavier Gouchet
 */
@Module
public class RepositoryModule {

    @Provides
    @ApplicationScope
    public ArticleRepository provideArticleRepository(@ApplicationScope OkHttpClient client) {
        return new ArticleRepository(client);
    }

    @Provides
    @ApplicationScope
    public FavoriteArticleRepository provideFavoriteArticleRepository(@ApplicationContext Context context) {
        return new FavoriteArticleRepository(context);
    }

    @Provides
    @ApplicationScope
    public DrawerArticleRepository provideDrawerArticleRepository(@ApplicationContext Context context) {
        return new DrawerArticleRepository(context);
    }
}
