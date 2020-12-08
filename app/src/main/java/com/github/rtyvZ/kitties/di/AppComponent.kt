package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        ProvideContextModule::class,
        BuilderModule::class,
        FirebaseModule::class,
        LaunchModule::class,
        RandomCatsFragmentModule::class,
        FavoriteCatsModule::class,
        MyCatsModule::class,
        SendCatModule::class,
        TakeAPhotoModule::class,
        MainActivityModule::class,
        NetworkModule::class]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: App?)
}

