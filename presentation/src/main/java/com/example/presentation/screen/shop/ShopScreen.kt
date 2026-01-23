package com.example.presentation.screen.shop

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.types.EquipCategory
import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.model.feature.types.ItemCategory
import com.example.domain.model.feature.types.ItemEffectType
import com.example.domain.model.feature.types.ItemFilterType
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ShopScreen(
    navController: NavController,
    argument: ShopArgument,
    data: ShopData
) {
    val context = LocalContext.current

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is ShopEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is ShopEvent.Error -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    if (data.selectedItemToBuy != null) {
        val maxBuyable = (data.userGold / data.selectedItemToBuy.buyPrice).toInt()
        val itemImage = getItemIcon(data.selectedItemToBuy.category)
        BuyDialog(
            imageUrl = itemImage,
            name = data.selectedItemToBuy.name,
            price = data.selectedItemToBuy.buyPrice,
            maxQuantity = 99,
            disableBuyReason = if (maxBuyable < 1) "골드가 부족합니다" else null,
            onDismiss = { argument.intent(ShopIntent.OnConfirmBuyItem(0, 0)) },
            onBuy = { quantity ->
                argument.intent(ShopIntent.OnConfirmBuyItem(data.selectedItemToBuy.id, quantity))
            }
        )
    }

    if (data.selectedEquipToBuy != null) {
        val canBuy = data.userGold >= data.selectedEquipToBuy.buyPrice
        val equipImage = getEquipIcon(data.selectedEquipToBuy.category)
        BuyDialog(
            imageUrl = equipImage,
            name = data.selectedEquipToBuy.name,
            price = data.selectedEquipToBuy.buyPrice,
            maxQuantity = 1,
            disableBuyReason = if (!canBuy) "골드가 부족합니다" else null,
            onDismiss = { argument.intent(ShopIntent.OnConfirmBuyEquip(0)) },
            onBuy = { _ ->
                argument.intent(ShopIntent.OnConfirmBuyEquip(data.selectedEquipToBuy.id))
            }
        )
    }

    ShopScreenContent(
        data = data,
        onIntent = argument.intent,
        onBackClick = { navController.popBackStack() }
    )
}

@Composable
fun ShopScreenContent(
    data: ShopData,
    onIntent: (ShopIntent) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(ShopTab.ITEM) }

    var filterExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "상점",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                    IconButton(onClick = { /* Info */ }) {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_shop),
                    contentDescription = "Shop Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    color = Color.White.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${data.userGold} G",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            SecondaryTabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = Color(0xFF878787),
                indicator = { }
            ) {
                ShopTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                text = tab.displayName,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier
                            .height(50.dp)
                            .background(
                                if (selectedTab == tab) Color(0xFFC3C3C3) else Color.Transparent
                            )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "판매 목록",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { filterExpanded = true }
                                .padding(4.dp)
                        ) {
                            Text(
                                text = if (selectedTab == ShopTab.ITEM) data.itemFilter.displayName else data.equipFilter.displayName,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Filter",
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(20.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = filterExpanded,
                            onDismissRequest = { filterExpanded = false }
                        ) {
                            if (selectedTab == ShopTab.ITEM) {
                                ItemFilterType.values().forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.displayName) },
                                        onClick = {
                                            onIntent(ShopIntent.OnItemFilterChange(type))
                                            filterExpanded = false
                                        }
                                    )
                                }
                            } else {
                                EquipFilterType.values().forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.displayName) },
                                        onClick = {
                                            onIntent(ShopIntent.OnEquipFilterChange(type))
                                            filterExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "|", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))

                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { sortExpanded = true }
                                .padding(4.dp)
                        ) {
                            Text(
                                text = if (selectedTab == ShopTab.ITEM) data.itemSort.displayName else data.equipSort.displayName,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Sort",
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(20.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false }
                        ) {
                            ShopSortType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        if (selectedTab == ShopTab.ITEM) {
                                            onIntent(ShopIntent.OnItemSortChange(type))
                                        } else {
                                            onIntent(ShopIntent.OnEquipSortChange(type))
                                        }
                                        sortExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            when (selectedTab) {
                ShopTab.ITEM -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(data.items) { item ->
                            val imageRes = getItemIcon(item.category)
                            ShopListItem(
                                imageUrl = imageRes,
                                name = item.name,
                                description = item.shortDescription,
                                price = item.buyPrice,
                                onClick = { onIntent(ShopIntent.OnBuyItemClick(item.id)) }
                            )
                        }
                    }
                }
                ShopTab.EQUIP -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(data.baseEquips) { equip ->
                            val imageRes = getEquipIcon(equip.category)
                            ShopListItem(
                                imageUrl = imageRes,
                                name = equip.name,
                                description = equip.description,
                                price = equip.buyPrice,
                                onClick = { onIntent(ShopIntent.OnBuyEquipClick(equip.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShopListItem(
    imageUrl: Int,
    name: String,
    description: String,
    price: Long,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = imageUrl),
                        contentDescription = name,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "가격 : $price G",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

private enum class ShopTab(val displayName: String) {
    ITEM("Item"),
    EQUIP("Equip")
}

private fun getItemIcon(category: ItemCategory): Int {
    return when (category) {
        ItemCategory.HEALING -> R.drawable.ic_potion_red
        ItemCategory.BUFF -> R.drawable.ic_potion_red
        ItemCategory.SCROLL -> R.drawable.ic_potion_red
        ItemCategory.INGREDIENT -> R.drawable.ic_potion_red
        ItemCategory.ETC -> R.drawable.ic_potion_red
    }
}

private fun getEquipIcon(category: EquipCategory): Int {
    return when (category) {
        EquipCategory.WEAPON -> R.drawable.ic_ironsword
        EquipCategory.SIDEARM -> R.drawable.ic_ironsword
        EquipCategory.ACCESSORY -> R.drawable.ic_ironsword
        EquipCategory.ARMOR -> R.drawable.ic_ironsword
        EquipCategory.GLOVES -> R.drawable.ic_ironsword
        EquipCategory.SHOES -> R.drawable.ic_ironsword
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    val dummyItems = List(10) { index ->
        Item(
            id = index.toLong(),
            name = "회복 물약 $index",
            shortDescription = "체력을 회복시켜주는 물약입니다. 아주 맛있습니다.",
            fullDescription = "",
            category = ItemCategory.HEALING,
            weight = 1.0,
            maxStackSize = 99,
            obtainMethods = emptyList(),
            isSellable = true,
            buyPrice = 100L * (index + 1),
            sellPrice = 50L,
            isUsable = true,
            effectType = ItemEffectType.HEAL,
            effectAmount = 50L
        )
    }

    AutoAdventureTheme {
        ShopScreen(
            navController = NavController(LocalContext.current),
            argument = ShopArgument(
                state = ShopState.Success,
                event = MutableSharedFlow(),
                intent = {}
            ),
            data = ShopData.init().copy(
                userGold = 50000,
                items = dummyItems
            )
        )
    }
}