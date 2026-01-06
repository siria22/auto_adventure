package com.example.presentation.screen.item

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.domain.model.feature.inventory.InventoryItem
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.molecule.item.EquipCard
import com.example.presentation.component.ui.molecule.item.ItemCard
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navController: NavController,
    itemArgument: ItemArgument,
    itemData: ItemData,
    equipData: EquipData,
    onEquipIntent: (ItemIntent) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Item", "Equip")

    var showDialog by remember { mutableStateOf(false) }
    var showEquipDialog by remember { mutableStateOf(false) }
    var selectedInventoryItem by remember { mutableStateOf<InventoryItem?>(null) }

    var showSellDialog by remember { mutableStateOf(false) }
    var sellTarget by remember { mutableStateOf<SellTarget?>(null) }


    val selectedItemDetail = itemArgument.selectedItemDetail

    val context = LocalContext.current

    LaunchedEffect(itemArgument.event) {
        itemArgument.event.collect { event ->
            if (event is ItemEvent.DataFetch.Error) {
                Toast.makeText(context, event.userMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    var itemFilterMenuExpanded by remember { mutableStateOf(false) }
    var itemSortMenuExpanded by remember { mutableStateOf(false) }
    var equipFilterMenuExpanded by remember { mutableStateOf(false) }
    var equipSortMenuExpanded by remember { mutableStateOf(false) }


    if (showDialog && selectedInventoryItem != null && selectedItemDetail != null) {
        ItemDetailDialog(
            itemName = selectedItemDetail.name,
            itemShortDesc = selectedItemDetail.shortDescription,
            itemFullDesc = selectedItemDetail.fullDescription,
            itemObtainMethod = selectedItemDetail.obtainMethods.joinToString(),
            quantity = selectedInventoryItem!!.amount.toInt(),
            onDismiss = { showDialog = false },
            onSellClick = {
                sellTarget = SellTarget.Item(selectedInventoryItem!!, selectedItemDetail)
                showSellDialog = true
                showDialog = false
            }
        )
    }

    if (showEquipDialog && itemArgument.selectedEquipDetail != null) {
        EquipDetailDialog(
            equipDetail = itemArgument.selectedEquipDetail!!,
            onDismiss = { showEquipDialog = false },
            onSellClick = {
                sellTarget = SellTarget.Equip(itemArgument.selectedEquipDetail!!)
                showSellDialog = true
                showEquipDialog = false
            }
        )
    }

    if (showSellDialog && sellTarget != null) {
        val target = sellTarget!!
        when (target) {
            is SellTarget.Item -> {
                SellDialog(
                    imageUrl = R.drawable.ic_potion_red, // TODO: 실제 아이템 이미지 매핑 필요
                    name = target.itemDetail.name,
                    price = target.itemDetail.sellPrice,
                    maxQuantity = target.inventoryItem.amount.toInt(),
                    onDismiss = { showSellDialog = false },
                    onSell = { quantity ->
                        itemArgument.intent(
                            ItemIntent.OnSellItem(
                                target.inventoryItem.itemId,
                                quantity
                            )
                        )
                        showSellDialog = false
                    }
                )
            }

            is SellTarget.Equip -> {
                SellDialog(
                    imageUrl = R.drawable.ic_ironsword, // TODO: 실제 장비 이미지 매핑 필요
                    name = target.equipDetail.name,
                    price = target.equipDetail.sellPrice,
                    maxQuantity = 1,
                    onDismiss = { showSellDialog = false },
                    onSell = { _ ->
                        onEquipIntent(ItemIntent.OnSellEquip(target.equipDetail.id))
                        showSellDialog = false
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDEDEDE),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "인벤토리",
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Image(
                painter = painterResource(id = R.drawable.bg_shop),
                contentDescription = "Shop Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFF878787),
                indicator = { }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title, fontSize = 25.sp) },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Black,
                        modifier = Modifier.background(
                            if (selectedTabIndex == index) Color(0xFFC3C3C3) else Color.Transparent
                        )
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> { // 아이템 탭 컨텐츠
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { itemFilterMenuExpanded = true }
                            ) {
                                Text(
                                    text = itemData.selectedFilter.displayName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Filter")
                            }
                            DropdownMenu(
                                expanded = itemFilterMenuExpanded,
                                onDismissRequest = { itemFilterMenuExpanded = false }
                            ) {
                                ItemFilterType.values().forEach { filterType ->
                                    DropdownMenuItem(
                                        text = { Text(filterType.displayName) },
                                        onClick = {
                                            itemArgument.intent(ItemIntent.OnFilterChange(filterType))
                                            itemFilterMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { itemSortMenuExpanded = true }
                            ) {
                                Text(
                                    text = itemData.selectedSort.displayName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Sort")
                            }
                            DropdownMenu(
                                expanded = itemSortMenuExpanded,
                                onDismissRequest = { itemSortMenuExpanded = false }
                            ) {
                                ItemSortType.values().forEach { sortType ->
                                    DropdownMenuItem(
                                        text = { Text(sortType.displayName) },
                                        onClick = {
                                            itemArgument.intent(ItemIntent.OnSortChange(sortType))
                                            itemSortMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(itemData.displayedItems) { item ->
                            Box(modifier = Modifier.clickable {
                                itemArgument.intent(ItemIntent.OnItemClick(item.itemId))
                                selectedInventoryItem = item
                                showDialog = true
                            }) {
                                ItemCard(
                                    imageUrl = R.drawable.ic_potion_red,
                                    quantity = item.amount.toInt()
                                )
                            }
                        }
                    }
                }

                1 -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { equipFilterMenuExpanded = true }
                            ) {
                                Text(
                                    text = equipData.selectedFilter.displayName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Filter")
                            }
                            DropdownMenu(
                                expanded = equipFilterMenuExpanded,
                                onDismissRequest = { equipFilterMenuExpanded = false }
                            ) {
                                EquipFilterType.values().forEach { filterType ->
                                    DropdownMenuItem(
                                        text = { Text(filterType.displayName) },
                                        onClick = {
                                            onEquipIntent(ItemIntent.OnEquipFilterChange(filterType))
                                            equipFilterMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { equipSortMenuExpanded = true }
                            ) {
                                Text(
                                    text = equipData.selectedSort.displayName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Sort")
                            }
                            DropdownMenu(
                                expanded = equipSortMenuExpanded,
                                onDismissRequest = { equipSortMenuExpanded = false }
                            ) {
                                EquipSortType.values().forEach { sortType ->
                                    DropdownMenuItem(
                                        text = { Text(sortType.displayName) },
                                        onClick = {
                                            onEquipIntent(ItemIntent.OnEquipSortChange(sortType))
                                            equipSortMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(equipData.displayedEquipments) { displayedEquip ->
                            Box(modifier = Modifier.clickable {
                                onEquipIntent(ItemIntent.OnEquipClick(displayedEquip.customizedId))
                                showEquipDialog = true
                            }) {
                                EquipCard(
                                    imageUrl = R.drawable.ic_ironsword,
                                    reinforcement = displayedEquip.reinforcement,
                                    isEquipped = displayedEquip.customizedId % 2 == 1L // TODO: 실제 장착 상태 로직 필요 (현재는 임시)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private sealed interface SellTarget {
    data class Item(
        val inventoryItem: InventoryItem,
        val itemDetail: com.example.domain.model.feature.inventory.Item
    ) : SellTarget

    data class Equip(
        val equipDetail: EquipDetail
    ) : SellTarget
}

@Preview(showBackground = true)
@Composable
private fun ItemScreenPreview() {
    val dummyArgument = ItemArgument(
        state = ItemState.Init,
        intent = { },
        event = MutableSharedFlow(),
        selectedItemDetail = null
    )
    val dummyItemData = ItemData(
        displayedItems = (1..20).map {
            InventoryItem(
                itemId = it.toLong(),
                partyId = 1,
                amount = (1..99).random().toLong()
            )
        },
        selectedFilter = ItemFilterType.ALL,
        selectedSort = ItemSortType.DEFAULT,
        totalWeight = 15.5,
        maxWeight = 100.0
    )
    val dummyEquipData = EquipData.empty()

    AutoAdventureTheme {
        ItemScreen(
            navController = NavController(LocalContext.current),
            itemArgument = dummyArgument,
            itemData = dummyItemData,
            equipData = dummyEquipData,
            onEquipIntent = {}
        )
    }
}