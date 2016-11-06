package fr.xgouchet.scparchive.inject.modules;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import fr.xgouchet.scparchive.commons.NetworkWatcher;
import fr.xgouchet.scparchive.inject.annotations.ApplicationContext;
import fr.xgouchet.scparchive.inject.annotations.ApplicationScope;
import fr.xgouchet.scparchive.inject.annotations.OfflineInterceptor;
import fr.xgouchet.scparchive.inject.annotations.OnlineInterceptor;
import fr.xgouchet.scparchive.inject.annotations.TypefaceForCaption;
import fr.xgouchet.scparchive.inject.annotations.TypefaceForText;
import fr.xgouchet.scparchive.inject.annotations.TypefaceForTitle;
import fr.xgouchet.scparchive.network.CacheInterceptor;
import fr.xgouchet.scparchive.network.NetworkInterceptor;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author Xavier Gouchet
 */
@Module
public class GlobalModule {

    public static final String FONTS_CARBON_TTF = "fonts/carbon.ttf";
    public static final String FONTS_1942_TTF = "fonts/1942.ttf";
    public static final String FONTS_DAISYWHL_TTF = "fonts/daisywhl.ttf";

    private static final long CACHE_SIZE_BYTES = 16 * 1024 * 1024;


    @NonNull private final Context appContext;

    public GlobalModule(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @ApplicationContext
    @ApplicationScope
    @NonNull
    public Context provideAppContext() {
        return appContext;
    }

    @Provides
    @OfflineInterceptor
    @NonNull
    public Interceptor provideOfflineInterceptor(@ApplicationContext Context context) {
        return new CacheInterceptor(context, new NetworkWatcher());
    }

    @Provides
    @OnlineInterceptor
    @NonNull
    public Interceptor provideOnlineInterceptor() {
        return new NetworkInterceptor();
    }

    @Provides
    @ApplicationScope
    @NonNull
    public OkHttpClient provideHttpClient(@ApplicationContext Context context,
                                          @NonNull @OnlineInterceptor Interceptor onlineInterceptor,
                                          @NonNull @OfflineInterceptor Interceptor offlineInterceptor) {

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(new Cache(context.getCacheDir(), CACHE_SIZE_BYTES));
        builder.addNetworkInterceptor(onlineInterceptor);
        builder.addInterceptor(offlineInterceptor);
        builder.build();

        final OkHttpClient build = builder.build();


        return build;
    }

    @Provides
    @ApplicationScope
    @TypefaceForText
    @NonNull Typeface provideTypefaceForText(@ApplicationContext Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONTS_DAISYWHL_TTF);
    }

    @Provides
    @ApplicationScope
    @TypefaceForTitle
    @NonNull Typeface provideTypefaceForTitle(@ApplicationContext Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONTS_1942_TTF);
    }

    @Provides
    @ApplicationScope
    @TypefaceForCaption
    @NonNull Typeface provideTypefaceForCaption(@ApplicationContext Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONTS_CARBON_TTF);
    }
}
