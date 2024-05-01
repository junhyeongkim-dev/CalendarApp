package com.android.calendarapp.library.security.di

import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.PrefStorageProviderImpl
import com.android.calendarapp.library.security.tink.helper.ITinkHelper
import com.android.calendarapp.library.security.tink.helper.TinkHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesHelper(prefStorageProviderImpl: PrefStorageProviderImpl) : PrefStorageProvider

    @Binds
    @Singleton
    abstract fun bindTinkHelper(tinkHelper: TinkHelper) : ITinkHelper
}