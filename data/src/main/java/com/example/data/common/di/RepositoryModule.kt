package com.example.data.common.di

import com.example.data.remote.feature.actor.repository.ActorActionRepositoryImpl
import com.example.data.remote.feature.actor.repository.ActorRepositoryImpl
import com.example.data.remote.feature.actor.repository.ActorSkillRepositoryImpl
import com.example.data.remote.feature.actor.repository.JobRepositoryImpl
import com.example.data.remote.feature.actor.repository.PersonalityRepositoryImpl
import com.example.data.remote.feature.actor.repository.SkillRepositoryImpl
import com.example.data.remote.feature.guild.repository.GuildRepositoryImpl
import com.example.data.remote.feature.inventory.repository.BaseEquipRepositoryImpl
import com.example.data.remote.feature.inventory.repository.CustomizedEquipRepositoryImpl
import com.example.data.remote.feature.inventory.repository.InventoryRepositoryImpl
import com.example.data.remote.feature.inventory.repository.ItemRepositoryImpl
import com.example.data.remote.nonfeature.log.repository.LogRepositoryImpl
import com.example.data.remote.feature.party.repository.PartyActionRepositoryImpl
import com.example.data.remote.feature.party.repository.PartyMemberRepositoryImpl
import com.example.data.remote.feature.party.repository.PartyRepositoryImpl
import com.example.data.remote.feature.quest.repository.AcceptedQuestRepositoryImpl
import com.example.data.remote.feature.quest.repository.QuestRepositoryImpl
import com.example.domain.repository.feature.actor.ActorActionRepository
import com.example.domain.repository.feature.actor.ActorRepository
import com.example.domain.repository.feature.actor.ActorSkillRepository
import com.example.domain.repository.feature.actor.JobRepository
import com.example.domain.repository.feature.actor.PersonalityRepository
import com.example.domain.repository.feature.actor.SkillRepository
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import com.example.domain.repository.feature.party.PartyActionRepository
import com.example.domain.repository.feature.party.PartyMemberRepository
import com.example.domain.repository.feature.party.PartyRepository
import com.example.domain.repository.feature.quest.AcceptedQuestRepository
import com.example.domain.repository.feature.quest.QuestRepository
import com.example.domain.repository.nonfeature.log.LogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Actor
    @Binds
    @Singleton
    abstract fun bindActorRepository(impl: ActorRepositoryImpl): ActorRepository

    @Binds
    @Singleton
    abstract fun bindActorActionRepository(impl: ActorActionRepositoryImpl): ActorActionRepository

    @Binds
    @Singleton
    abstract fun bindActorSkillRepository(impl: ActorSkillRepositoryImpl): ActorSkillRepository

    @Binds
    @Singleton
    abstract fun bindJobRepository(impl: JobRepositoryImpl): JobRepository

    @Binds
    @Singleton
    abstract fun bindPersonalityRepository(impl: PersonalityRepositoryImpl): PersonalityRepository

    @Binds
    @Singleton
    abstract fun bindSkillRepository(impl: SkillRepositoryImpl): SkillRepository

    // Party
    @Binds
    @Singleton
    abstract fun bindPartyRepository(impl: PartyRepositoryImpl): PartyRepository

    @Binds
    @Singleton
    abstract fun bindPartyActionRepository(impl: PartyActionRepositoryImpl): PartyActionRepository

    @Binds
    @Singleton
    abstract fun bindPartyMemberRepository(impl: PartyMemberRepositoryImpl): PartyMemberRepository

    // Inventory
    @Binds
    @Singleton
    abstract fun bindInventoryRepository(impl: InventoryRepositoryImpl): InventoryRepository

    @Binds
    @Singleton
    abstract fun bindItemRepository(impl: ItemRepositoryImpl): ItemRepository

    @Binds
    @Singleton
    abstract fun bindBaseEquipRepository(impl: BaseEquipRepositoryImpl): BaseEquipRepository

    @Binds
    @Singleton
    abstract fun bindCustomizedEquipRepository(impl: CustomizedEquipRepositoryImpl): CustomizedEquipRepository

    // Quest
    @Binds
    @Singleton
    abstract fun bindQuestRepository(impl: QuestRepositoryImpl): QuestRepository

    @Binds
    @Singleton
    abstract fun bindAcceptedQuestRepository(impl: AcceptedQuestRepositoryImpl): AcceptedQuestRepository

    // Guild
    @Binds
    @Singleton
    abstract fun bindGuildRepository(impl: GuildRepositoryImpl): GuildRepository

    //Log
    @Binds
    @Singleton
    abstract fun bindLogRepository(impl: LogRepositoryImpl): LogRepository
}