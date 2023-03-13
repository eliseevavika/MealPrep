package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.MealPrepTheme
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun NavigationBarCreation(navController: NavController) {
    val items = listOf(
        NavigationItemCreation.Intro,
        NavigationItemCreation.Ingredients,
        NavigationItemCreation.Steps
    )
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,

        ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        items.forEach { item ->
            val currentRoute = navBackStackEntry?.destination?.route
            val selected = currentRoute == item.route

            BottomNavigationItem(
                icon = { },
                label = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = item.title,
                        fontFamily = fontFamilyForBodyB2,
                        fontSize = 16.sp,
                        color = if (selected) MealPrepColor.white else MealPrepColor.grey_800,

                        modifier = Modifier
                            .drawBehind {
                                drawRoundRect(
                                    color = if (selected) MealPrepColor.orange else MealPrepColor.transparent,
                                    cornerRadius = CornerRadius(x = 100f, y = 100f),
                                )
                            }
                            .padding(
                                start = NavigationItemCreation.Ingredients.title.length.minus(
                                    item.title.length
                                ).dp.plus(10.dp),
                                end = NavigationItemCreation.Ingredients.title.length.minus(item.title.length).dp.plus(
                                    10.dp
                                ),
                                top = 8.dp,
                                bottom = 8.dp
                            ),

                        )
                },
                selectedContentColor = MealPrepColor.orange,
                unselectedContentColor = MealPrepColor.black,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
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
                }
            )
        }
    }
}



