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

    val itemsRouts = listOf(
        NavigationItem.Home.route,
        NavigationItem.MealPrep.route,
        NavigationItem.Groceries.route,
        NavigationItem.Settings.route,
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
                val isBottomNavRoute = itemsRouts.contains(currentRoute)

                BottomNavigationItem(
                    icon = {
                        if(isBottomNavRoute){
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title,
                                tint = color()
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title, color = color()
                        )
                    },
                    enabled = isBottomNavRoute ,
                    selectedContentColor = MealPrepColor.orange,
                    unselectedContentColor = MealPrepColor.black,
                    alwaysShowLabel = isBottomNavRoute,
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


@Composable
fun ShowIconForNavBar(icon: () -> Int, title: () -> String, selected: Boolean, color: Color) {
    Icon(
        painterResource(id = icon()), contentDescription = title(), tint = color
    )
}

@Composable
fun ShowTextForNavBar(title: () -> String, selected: Boolean, color: Color) {
    Text(
        text = title(), color = color
    )
}