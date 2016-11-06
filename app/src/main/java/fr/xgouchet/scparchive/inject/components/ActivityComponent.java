package fr.xgouchet.scparchive.inject.components;

import dagger.Component;
import fr.xgouchet.scparchive.inject.annotations.ActivityScope;
import fr.xgouchet.scparchive.inject.modules.PresenterModule;

/**
 * @author Xavier Gouchet
 */
@Component(dependencies = AppComponent.class, modules = {PresenterModule.class})
@ActivityScope
public interface ActivityComponent {

//    ProjectListPresenter getProjectListPresenter();


}
