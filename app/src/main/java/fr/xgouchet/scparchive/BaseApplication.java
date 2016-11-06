package fr.xgouchet.scparchive;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import net.danlew.android.joda.JodaTimeAndroid;

import fr.xgouchet.scparchive.inject.components.AppComponent;
import fr.xgouchet.scparchive.inject.components.DaggerAppComponent;
import fr.xgouchet.scparchive.inject.modules.GlobalModule;
import fr.xgouchet.scparchive.inject.modules.RepositoryModule;

/**
 * @author Xavier Gouchet
 */
public class BaseApplication extends Application {

    private AppComponent appComponent;

    public static BaseApplication from(@NonNull Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .globalModule(new GlobalModule(this))
                .repositoryModule(new RepositoryModule())
                .build();

        JodaTimeAndroid.init(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
