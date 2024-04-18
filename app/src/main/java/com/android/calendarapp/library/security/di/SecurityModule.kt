package com.android.calendarapp.library.security.di

import com.android.calendarapp.library.security.preperence.AppEncryptedSharedPreferences
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.library.security.preperence.helper.SharedPreferencesHelper
import com.android.calendarapp.library.security.tink.AppTink
import com.android.calendarapp.library.security.tink.helper.ITinkHelper
import com.android.calendarapp.library.security.tink.helper.TinkHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesHelper(preferencesHelper: SharedPreferencesHelper) : ISharedPreferencesHelper

    @Binds
    @Singleton
    abstract fun bindTinkHelper(tinkHelper: TinkHelper) : ITinkHelper
}