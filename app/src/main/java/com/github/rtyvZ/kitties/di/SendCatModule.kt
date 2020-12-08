package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.db.CatDatabase
import com.github.rtyvZ.kitties.repositories.sendPhoto.SendPhotoRepository
import dagger.Module
import dagger.Provides

@Module
class SendCatModule {

    @Provides
    internal fun provideSendCatRepo(
        sessionStorage: UserInternalStorageContract,
        api: Api,
        db: CatDatabase
    ): SendPhotoRepository {
        return SendPhotoRepository(sessionStorage, api, db)
    }
}