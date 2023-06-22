package com.example.mealprep

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mealprep.ui.theme.MealPrepColor

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.MealPrep,
        NavigationItem.Groceries,
        NavigationItem.Settings,
    )

    BottomNavigation(
        backgroundColor = Color.White, contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            key(item.route) {
                val selected = currentRoute == item.route
                val color = if (selected) MealPrepColor.orange else Color.Black

                BottomNavigationItem(
                    icon = {
                        ShowIconForNavBar(item, selected, color)
                    },
                    label = {
                        ShowTextForNavBar(item, selected, color)
                    },
                    selectedContentColor = MealPrepColor.orange,
                    unselectedContentColor = MealPrepColor.black,
                    alwaysShowLabel = true,
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    interactionSource = NoRippleInteractionSource()
                )
            }

        }
    }
}

@Composable
fun ShowIconForNavBar(item: NavigationItem, selected: Boolean, color: Color) {
    Icon(
        painterResource(id = item.icon), contentDescription = item.title, tint = color
    )
}

@Composable
fun ShowTextForNavBar(item: NavigationItem, selected: Boolean, color: Color) {
    Text(
        text = item.title, color = color
    )
}