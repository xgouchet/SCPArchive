package fr.xgouchet.scparchive.inject.components;

import android.content.Context;
import android.graphics.Typeface;

import dagger.Component;
import fr.xgouchet.scparchive.inject.annotations.ApplicationContext;
import fr.xgouchet.scparchive.inject.annotations.ApplicationScope;
import fr.xgouchet.scparchive.inject.annotations.TypefaceForCaption;
import fr.xgouchet.scparchive.inject.annotations.TypefaceForText;
import fr.xgouchet.scparchive.inject.annotations.TypefaceForTitle;
import fr.xgouchet.scparchive.inject.modules.GlobalModule;
import fr.xgouchet.scparchive.inject.modules.RepositoryModule;
import fr.xgouchet.scparchive.network.ArticleRepository;
import fr.xgouchet.scparchive.network.DrawerArticleRepository;
import fr.xgouchet.scparchive.network.FavoriteArticleRepository;
import okhttp3.OkHttpClient;

/**
 * @author Xavier Gouchet
 */
@Component(modules = {
        GlobalModule.class,
        RepositoryModule.class})
@ApplicationScope
public interface AppComponent {

    @ApplicationContext
    Context getApplicationContext();

    // NETWORK

    OkHttpClient getHttpClient();

    // REPOSITORIES

    ArticleRepository getArticleRepository();
    FavoriteArticleRepository getFavoriteArticleRepository();
    DrawerArticleRepository getDrawerArticleRepository();

    // MISC

    @TypefaceForCaption Typeface getTypefaceForCaption();
    @TypefaceForText Typeface getTypefaceForText();
    @TypefaceForTitle Typeface getTypefaceForTitle();
}
