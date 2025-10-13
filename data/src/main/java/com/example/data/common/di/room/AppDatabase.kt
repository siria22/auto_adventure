package com.example.data.common.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.remote.feature.actor.dao.ActorActionDao
import com.example.data.remote.feature.actor.dao.ActorDao
import com.example.data.remote.feature.actor.dao.ActorSkillDao
import com.example.data.remote.feature.actor.entity.ActorActionEntity
import com.example.data.remote.feature.actor.entity.ActorEntity
import com.example.data.remote.feature.actor.entity.ActorSkillEntity
import com.example.data.remote.feature.inventory.dao.CustomizedEquipDao
import com.example.data.remote.feature.inventory.dao.InventoryItemDao
import com.example.data.remote.feature.inventory.entity.CustomizedEquipEntity
import com.example.data.remote.feature.inventory.entity.InventoryItemEntity
import com.example.data.remote.feature.party.dao.PartyActionDao
import com.example.data.remote.feature.party.dao.PartyDao
import com.example.data.remote.feature.party.dao.PartyMemberDao
import com.example.data.remote.feature.party.entity.PartyActionEntity
import com.example.data.remote.feature.party.entity.PartyEntity
import com.example.data.remote.feature.party.entity.PartyMemberEntity
import com.example.data.remote.feature.quest.dao.AcceptedQuestDao
import com.example.data.remote.feature.quest.entity.AcceptedQuestEntity

@Database(
    entities = [
        ActorActionEntity::class, ActorEntity::class, ActorSkillEntity::class,
        CustomizedEquipEntity::class, InventoryItemEntity::class,
        PartyActionEntity::class, PartyEntity::class, PartyMemberEntity::class,
        AcceptedQuestEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actorDao(): ActorDao
    abstract fun actorActionDao(): ActorActionDao
    abstract fun actorSkillDao(): ActorSkillDao

    abstract fun partyDao(): PartyDao
    abstract fun partyActionDao(): PartyActionDao
    abstract fun partyMemberDao(): PartyMemberDao

    abstract fun inventoryDao(): InventoryItemDao
    abstract fun customizedEquipDao(): CustomizedEquipDao

    abstract fun acceptedQuestDao(): AcceptedQuestDao
}