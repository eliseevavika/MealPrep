package com.example.mealprep

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mealprep.ui.theme.MealPrepColor

@Composable
fun BottomNavigationBar(navController: () -> NavHostController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.MealPrep,
        NavigationItem.Groceries,
        NavigationItem.Settings,
    )

    BottomNavigation(
        backgroundColor = Color.White, contentColor = Color.Black
    ) {
        val navBackStackEntry by navController().currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            key(items) {
                val selected = { currentRoute == item.route }
                val color = { if (selected()) MealPrepColor.orange else Color.Black }

                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = color()
                        )
                    },
                    label = {
                        Text(
                            text = item.title, color = color()
                        )
                    },
                    selectedContentColor = MealPrepColor.orange,
                    unselectedContentColor = MealPrepColor.black,
                    selected = selected(),
                    onClick = {
                        navController().navigate(item.route) {
                            navController().graph.startDestinationRoute?.let { route ->
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
