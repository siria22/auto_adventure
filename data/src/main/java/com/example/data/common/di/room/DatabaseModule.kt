package com.example.data.common.di.room

import android.content.Context
import androidx.room.Room
import com.example.data.remote.feature.actor.dao.ActorActionDao
import com.example.data.remote.feature.actor.dao.ActorDao
import com.example.data.remote.feature.actor.dao.ActorSkillDao
import com.example.data.remote.feature.inventory.dao.CustomizedEquipDao
import com.example.data.remote.feature.inventory.dao.InventoryItemDao
import com.example.data.remote.nonfeature.log.dao.LogDao
import com.example.data.remote.feature.party.dao.PartyActionDao
import com.example.data.remote.feature.party.dao.PartyDao
import com.example.data.remote.feature.party.dao.PartyMemberDao
import com.example.data.remote.feature.quest.dao.AcceptedQuestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideActorDao(database: AppDatabase): ActorDao {
        return database.actorDao()
    }

    @Provides
    fun provideActorActionDao(database: AppDatabase): ActorActionDao {
        return database.actorActionDao()
    }

    @Provides
    fun provideActorSkillDao(database: AppDatabase): ActorSkillDao {
        return database.actorSkillDao()
    }

    @Provides
    fun providePartyDao(database: AppDatabase): PartyDao {
        return database.partyDao()
    }

    @Provides
    fun providePartyActionDao(database: AppDatabase): PartyActionDao {
        return database.partyActionDao()
    }

    @Provides
    fun providePartyMemberDao(database: AppDatabase): PartyMemberDao {
        return database.partyMemberDao()
    }

    @Provides
    fun provideInventoryDao(database: AppDatabase): InventoryItemDao {
        return database.inventoryDao()
    }

    @Provides
    fun provideCustomizedEquipDao(database: AppDatabase): CustomizedEquipDao {
        return database.customizedEquipDao()
    }

    @Provides
    fun provideAcceptedQuestDao(database: AppDatabase): AcceptedQuestDao {
        return database.acceptedQuestDao()
    }

    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }
}