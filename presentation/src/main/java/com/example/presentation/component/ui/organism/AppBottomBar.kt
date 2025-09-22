package com.example.presentation.component.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.R
import com.example.presentation.utils.nav.safePopBackStack
import com.example.presentation.component.theme.SiriaTemplateColorScheme
import com.example.presentation.component.theme.SiriaTemplateTheme

/**
 * Bottom Navigation Bar
 *
 * @param selectedItem The currently selected item on the Bottom Navigation Bar
 * @param backgroundColor The background color of the Bottom Navigation Bar. Default is [Color.Transparent]
 * @param navController The instance of [NavController] used for navigation
 */
@Composable
fun BottomNavigationBar(
    selectedItem: CurrentBottomNav,
    backgroundColor: Color = Color.Transparent,
    navController: NavController
) {
    val navItemList = listOf(
        BottomNavInfo(
            label = "기록",
            iconResourceId = R.drawable.history,
            bottomNavType = CurrentBottomNav.HISTORY,
            onClicked = {
                // TODO : [Bottom Nav] Navigate to History Screen
                // navController.safeNavigate(ScreenDestinations.History.route)
            }
        ),
        BottomNavInfo(
            label = "홈",
            iconResourceId = R.drawable.home,
            bottomNavType = CurrentBottomNav.HOME,
            onClicked = {
                navController.safePopBackStack()
            }
        ),
        BottomNavInfo(
            label = "프로필",
            iconResourceId = R.drawable.account_circle,
            bottomNavType = CurrentBottomNav.PROFILE,
            onClicked = {
                // TODO : [Bottom Nav] Navigate to Profile Screen
                // navController.safeNavigate(ScreenDestinations.Profile.route)
            }
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor),
        verticalAlignment = Alignment.Bottom
    ) {
        navItemList.forEachIndexed { _, item ->
            BottomNavItem(
                iconRes = painterResource(item.iconResourceId),
                itemLabel = item.label,
                isSelected = (selectedItem == item.bottomNavType),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        item.onClicked
                    },
                onClicked = item.onClicked
            )
        }
    }
}
/* <<<<<<<<<<  a5d9158e-0c34-4c7c-9fce-08a383229082  >>>>>>>>>>> */

@Composable
private fun BottomNavItem(
    iconRes: Painter,
    itemLabel: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClicked: () -> Unit
) {
    val selectedColor = SiriaTemplateColorScheme.primary
    val unselectedColor = Color.White

    Column(
        modifier = modifier.clickable {
            onClicked()
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .background(if (isSelected) selectedColor else unselectedColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = iconRes,
                contentDescription = itemLabel,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = itemLabel,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (!isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

data class BottomNavInfo(
    val label: String,
    val iconResourceId: Int,
    val bottomNavType: CurrentBottomNav,
    val onClicked: () -> Unit
)

enum class CurrentBottomNav {
    HISTORY, HOME, PROFILE
}

@Preview
@Composable
private fun BottomNavigationBarPreview1() {
    SiriaTemplateTheme {
        BottomNavigationBar(
            selectedItem = CurrentBottomNav.HOME,
            navController = rememberNavController()
        )
    }
}