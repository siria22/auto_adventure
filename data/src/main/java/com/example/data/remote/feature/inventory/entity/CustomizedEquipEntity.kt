package com.example.data.remote.feature.inventory.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.data.remote.feature.actor.entity.ActorEntity

@Entity(
    tableName = "customized_equip",
    /*foreignKeys = [
        ForeignKey(
            entity = ActorEntity::class,
            parentColumns = ["actor_id"],
            childColumns = ["owner_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],*/
    indices = [Index(value = ["owner_id"])]
)
data class CustomizedEquipEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "owner_id")
    val ownerId: Long,

    @ColumnInfo(name = "equip_id")
    val equipId: Long,

    @ColumnInfo(name="category")
    val category: String,

    @ColumnInfo(name = "customized_name")
    val customizedName: String,

    @ColumnInfo(name = "required_strength")
    val requiredStrength: Long,

    @ColumnInfo(name = "required_agility")
    val requiredAgility: Long,

    @ColumnInfo(name = "required_intelligence")
    val requiredIntelligence: Long,

    @ColumnInfo(name = "required_luck")
    val requiredLuck: Long,

    @ColumnInfo(name = "increase_stat")
    val increaseStat: String,

    @ColumnInfo(name = "increase_amount")
    val increaseAmount: Long,

    @ColumnInfo(name = "reinforcement")
    val reinforcement: Long,

    @ColumnInfo(name = "modified_sell_price")
    val modifiedSellPrice: Long,

    @ColumnInfo(name = "modified_weight")
    val modifiedWeight: Long,

    @ColumnInfo(name = "rank")
    val rank: String
)