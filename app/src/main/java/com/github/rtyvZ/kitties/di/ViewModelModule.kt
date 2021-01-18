package com.github.rtyvZ.kitties.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.rtyvZ.kitties.auth.LaunchViewModel
import com.github.rtyvZ.kitties.base.ViewModelFactory
import com.github.rtyvZ.kitties.ui.catsBreeds.CatsBreedsViewModel
import com.github.rtyvZ.kitties.ui.favoriteCats.FavoriteCatsViewModel
import com.github.rtyvZ.kitties.ui.main.MainActivityViewModel
import com.github.rtyvZ.kitties.ui.myUploadedCats.MyCatsViewModel
import com.github.rtyvZ.kitties.ui.randomCats.RandomCatsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    abstract fun bindLaunchViewModel(launchViewModel: LaunchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RandomCatsViewModel::class)
    abstract fun bindRandomCatsViewModel(randomCatsViewModel: RandomCatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteCatsViewModel::class)
    abstract fun bindFavoriteCatsViewModel(randomCatsViewModel: FavoriteCatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyCatsViewModel::class)
    abstract fun bindMyCatsViewModel(myCatsViewModel: MyCatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CatsBreedsViewModel::class)
    abstract fun bindCatsBreedsViewModel(breedsViewModel: CatsBreedsViewModel):ViewModel

    @Binds
    @Singleton
    abstract fun funBindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


}