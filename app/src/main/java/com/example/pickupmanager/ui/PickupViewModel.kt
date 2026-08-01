package com.example.pickupmanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickupmanager.data.AppDatabase
import com.example.pickupmanager.data.PickupCode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PickupViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).pickupCodeDao()

    val allPickupCodes: StateFlow<List<PickupCode>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addPickupCode(
        code: String,
        company: String,
        location: String,
        note: String,
        arrivalDate: Long
    ) {
        viewModelScope.launch {
            dao.insert(
                PickupCode(
                    code = code,
                    company = company,
                    location = location,
                    note = note,
                    timestamp = arrivalDate
                )
            )
        }
    }

    fun updatePickupCode(
        item: PickupCode,
        code: String,
        company: String,
        location: String,
        note: String,
        arrivalDate: Long
    ) {
        viewModelScope.launch {
            dao.update(
                item.copy(
                    code = code,
                    company = company,
                    location = location,
                    note = note,
                    timestamp = arrivalDate
                )
            )
        }
    }

    fun togglePickedUp(item: PickupCode) {
        viewModelScope.launch {
            dao.updatePickedUpStatus(item.id, !item.isPickedUp)
        }
    }

    fun deletePickupCode(item: PickupCode) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }

    fun deletePickupCodes(items: List<PickupCode>) {
        if (items.isEmpty()) return
        viewModelScope.launch {
            dao.deleteAll(items)
        }
    }
}
