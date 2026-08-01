package com.example.pickupmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.runtime.Immutable

@Immutable
@Entity(tableName = "pickup_codes")
data class PickupCode(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,
    val company: String,
    val location: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isPickedUp: Boolean = false
)
