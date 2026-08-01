package com.example.pickupmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PickupCodeDao {
    @Query("SELECT * FROM pickup_codes ORDER BY timestamp DESC")
    fun getAll(): Flow<List<PickupCode>>

    @Insert
    suspend fun insert(pickupCode: PickupCode)

    @Update
    suspend fun update(pickupCode: PickupCode)

    @Delete
    suspend fun delete(pickupCode: PickupCode)

    @Delete
    suspend fun deleteAll(pickupCodes: List<PickupCode>)

    @Query("UPDATE pickup_codes SET isPickedUp = :pickedUp WHERE id = :id")
    suspend fun updatePickedUpStatus(id: Long, pickedUp: Boolean)
}
