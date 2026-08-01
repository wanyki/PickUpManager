package com.example.pickupmanager.ui

import androidx.compose.runtime.Immutable
import com.example.pickupmanager.data.PickupCode

internal enum class PickupFilter(val label: String) {
    ALL("全部"),
    PENDING("待取"),
    COMPLETED("已取")
}

@Immutable
internal data class PickupLocationGroup(
    val location: String,
    val items: List<PickupCode>
)

@Immutable
internal data class PickupListUiData(
    val totalCount: Int,
    val pendingCount: Int,
    val completedCount: Int,
    val visibleCodes: List<PickupCode>,
    val groups: List<PickupLocationGroup>
)

internal fun buildPickupListUiData(
    pickupCodes: List<PickupCode>,
    filter: PickupFilter
): PickupListUiData {
    val pendingCount = pickupCodes.count { !it.isPickedUp }
    val visibleCodes = when (filter) {
        PickupFilter.ALL -> pickupCodes
        PickupFilter.PENDING -> pickupCodes.filterNot { it.isPickedUp }
        PickupFilter.COMPLETED -> pickupCodes.filter { it.isPickedUp }
    }
    val groups = visibleCodes
        .groupBy { it.location.trim().ifBlank { "未填写地点" } }
        .map { (location, items) -> PickupLocationGroup(location, items) }

    return PickupListUiData(
        totalCount = pickupCodes.size,
        pendingCount = pendingCount,
        completedCount = pickupCodes.size - pendingCount,
        visibleCodes = visibleCodes,
        groups = groups
    )
}
