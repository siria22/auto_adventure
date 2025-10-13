package com.example.data.common.di.preference

import android.content.Context
import com.example.data.remote.feature.guild.dao.GuildPreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context): GuildPreferenceProvider {
        return GuildPreferenceProvider(context)
    }
}
