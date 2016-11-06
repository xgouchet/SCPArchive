package fr.xgouchet.scparchive.network;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import fr.xgouchet.scparchive.commons.NetworkWatcher;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Xavier Gouchet
 */
public class CacheInterceptor implements Interceptor {

    public static final int SEVEN_DAYS = 60 * 60 * 24 * 7;
    @NonNull private final Context context;
    @NonNull private final NetworkWatcher networkWatcher;

    public CacheInterceptor(@NonNull Context context,
                            @NonNull NetworkWatcher networkWatcher) {
        this.context = context;
        this.networkWatcher = networkWatcher;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!networkWatcher.isNetworkAvailable(context)) {
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + SEVEN_DAYS)
                    .build();
        }
        return chain.proceed(request);
    }
}
