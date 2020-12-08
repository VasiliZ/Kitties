package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.auth.LaunchActivity
import com.github.rtyvZ.kitties.ui.favoriteCats.FavoriteCatsFragment
import com.github.rtyvZ.kitties.ui.main.MainActivity
import com.github.rtyvZ.kitties.ui.myUploadedCats.MyCatFragment
import com.github.rtyvZ.kitties.ui.randomCats.RandomCatsFragment
import com.github.rtyvZ.kitties.ui.sendPhoto.TakePhotoActivity
import com.github.rtyvZ.kitties.ui.services.SendCatService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {

    @ContributesAndroidInjector(modules = [LaunchModule::class])
    internal abstract fun bindLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector(modules = [RandomCatsFragmentModule::class])
    internal abstract fun bindRandomCatsFragment(): RandomCatsFragment

    @ContributesAndroidInjector(modules = [FavoriteCatsModule::class])
    internal abstract fun bindFavoriteCatsFragment(): FavoriteCatsFragment

    @ContributesAndroidInjector(modules = [MyCatsModule::class])
    internal abstract fun bindMyCatsFragment(): MyCatFragment

    @ContributesAndroidInjector(modules = [SendCatModule::class])
    internal abstract fun bindSendCatService(): SendCatService

    @ContributesAndroidInjector(modules = [TakeAPhotoModule::class])
    internal abstract fun bindTakeAPhotoActivity(): TakePhotoActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun bindMainActivity(): MainActivity
}