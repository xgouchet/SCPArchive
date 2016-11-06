package fr.xgouchet.scparchive.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.inject.components.ActivityComponent;
import fr.xgouchet.scparchive.inject.components.AppComponent;
import fr.xgouchet.scparchive.inject.components.DaggerActivityComponent;
import fr.xgouchet.scparchive.inject.modules.PresenterModule;

/**
 * @author Xavier Gouchet
 */
public class BaseActivity extends AppCompatActivity {

    private AppComponent appComponent;
    private ActivityComponent activityComponent;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appComponent = BaseApplication.from(this).getAppComponent();
        activityComponent = DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .presenterModule(new PresenterModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
