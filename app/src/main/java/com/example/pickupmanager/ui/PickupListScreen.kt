package com.example.pickupmanager.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pickupmanager.data.PickupCode

@Composable
fun PickupListScreen(viewModel: PickupViewModel = viewModel()) {
    val pickupCodes by viewModel.allPickupCodes.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf(PickupFilter.ALL) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<PickupCode?>(null) }
    var actionItem by remember { mutableStateOf<PickupCode?>(null) }
    var batchMode by remember { mutableStateOf(false) }
    val selectedPickupIds = remember { mutableStateMapOf<Long, Unit>() }
    var showBatchDeleteConfirmation by remember { mutableStateOf(false) }

    // Filtering and grouping only run when database data or the selected tab changes.
    // Selection, dialogs and scrolling no longer rebuild these lists.
    val uiData = remember(pickupCodes, selectedFilter) {
        buildPickupListUiData(pickupCodes, selectedFilter)
    }

    fun leaveBatchMode() {
        batchMode = false
        selectedPickupIds.clear()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (!batchMode) {
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("添加取件码") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 104.dp
            ),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            item(key = "summary", contentType = "summary") {
                PickupSummary(
                    totalCount = uiData.totalCount,
                    pendingCount = uiData.pendingCount
                )
            }

            item(key = "filters", contentType = "filters") {
                PickupFilterBar(
                    selectedFilter = selectedFilter,
                    totalCount = uiData.totalCount,
                    pendingCount = uiData.pendingCount,
                    completedCount = uiData.completedCount,
                    onFilterSelected = { filter ->
                        selectedFilter = filter
                        leaveBatchMode()
                    }
                )
            }

            if (uiData.visibleCodes.isNotEmpty() && !batchMode) {
                item(key = "long-press-hint", contentType = "hint") {
                    LongPressHint(isCompletedList = selectedFilter == PickupFilter.COMPLETED)
                }
            }

            if (selectedFilter == PickupFilter.COMPLETED && batchMode) {
                item(key = "batch-actions", contentType = "batch-actions") {
                    BatchActionBar(
                        selectedCount = selectedPickupIds.size,
                        totalCount = uiData.visibleCodes.size,
                        onSelectAll = {
                            if (selectedPickupIds.size == uiData.visibleCodes.size) {
                                selectedPickupIds.clear()
                            } else {
                                selectedPickupIds.clear()
                                uiData.visibleCodes.forEach { selectedPickupIds[it.id] = Unit }
                            }
                        },
                        onCancel = ::leaveBatchMode,
                        onEdit = {
                            editingItem = uiData.visibleCodes.firstOrNull {
                                selectedPickupIds.containsKey(it.id)
                            }
                            leaveBatchMode()
                        },
                        onDelete = { showBatchDeleteConfirmation = true }
                    )
                }
            }

            if (uiData.visibleCodes.isEmpty()) {
                item(key = "empty", contentType = "empty") {
                    PickupEmptyState(
                        hasAnyItems = uiData.totalCount > 0,
                        onAdd = { showAddDialog = true }
                    )
                }
            } else {
                uiData.groups.forEach { group ->
                    item(
                        key = "location-${group.location}",
                        contentType = "location-header"
                    ) {
                        LocationGroupHeader(
                            location = group.location,
                            count = group.items.size
                        )
                    }
                    items(
                        items = group.items,
                        key = { it.id },
                        contentType = { "pickup-card" }
                    ) { item ->
                        val isSelected = selectedPickupIds.containsKey(item.id)
                        PickupCard(
                            item = item,
                            selectionMode = batchMode,
                            isSelected = isSelected,
                            onSelectionChange = { selected ->
                                if (selected) {
                                    selectedPickupIds[item.id] = Unit
                                } else {
                                    selectedPickupIds.remove(item.id)
                                }
                            },
                            onLongPress = {
                                if (selectedFilter == PickupFilter.COMPLETED) {
                                    batchMode = true
                                    selectedPickupIds[item.id] = Unit
                                } else {
                                    actionItem = item
                                }
                            },
                            onToggleStatus = { viewModel.togglePickedUp(item) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        PickupEditDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { code, company, location, note, arrivalDate ->
                viewModel.addPickupCode(code, company, location, note, arrivalDate)
                showAddDialog = false
            }
        )
    }

    editingItem?.let { item ->
        PickupEditDialog(
            item = item,
            onDismiss = { editingItem = null },
            onConfirm = { code, company, location, note, arrivalDate ->
                viewModel.updatePickupCode(item, code, company, location, note, arrivalDate)
                editingItem = null
            }
        )
    }

    actionItem?.let { item ->
        PickupItemActionsDialog(
            item = item,
            onDismiss = { actionItem = null },
            onEdit = {
                editingItem = item
                actionItem = null
            },
            onDelete = {
                viewModel.deletePickupCode(item)
                actionItem = null
            }
        )
    }

    if (showBatchDeleteConfirmation) {
        BatchDeleteConfirmationDialog(
            selectedCount = selectedPickupIds.size,
            onDismiss = { showBatchDeleteConfirmation = false },
            onConfirm = {
                viewModel.deletePickupCodes(
                    uiData.visibleCodes.filter { selectedPickupIds.containsKey(it.id) }
                )
                leaveBatchMode()
                showBatchDeleteConfirmation = false
            }
        )
    }
}
