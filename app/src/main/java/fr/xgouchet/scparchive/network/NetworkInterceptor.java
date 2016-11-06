package fr.xgouchet.scparchive.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Xavier Gouchet
 */
public class NetworkInterceptor implements Interceptor {

    public static final int SEVEN_DAYS = 60 * 60 * 24 * 7;

    @Override public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        final Response.Builder builder = originalResponse.newBuilder();
        final String pragma = originalResponse.header("Pragma");
        if (pragma.contains("no-cache")) {
            builder.removeHeader("Pragma");
        }

        // Overwrite cache Control
        String cacheControl = originalResponse.header("Cache-Control");
        if ((cacheControl == null)
                || cacheControl.contains("no-store")
                || cacheControl.contains("no-cache")
                || cacheControl.contains("must-revalidate")
                || cacheControl.contains("max-age=0")) {
            builder.header("Cache-Control", "public, max-age=" + SEVEN_DAYS);
        }

        return builder.build();
    }
}
