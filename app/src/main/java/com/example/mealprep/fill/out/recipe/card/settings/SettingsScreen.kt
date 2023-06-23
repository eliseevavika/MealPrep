package com.example.mealprep.fill.out.recipe.card.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mealprep.BottomNavigationBar
import com.example.mealprep.fill.out.recipe.card.TopBarForSettingsScreen
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel

@Composable
fun SettingsScreen(
    navController: () -> NavHostController,
    viewModal: () -> RecipeCreationViewModel
) {
    Scaffold(topBar = {
        TopBarForSettingsScreen()
    },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {

            }
        })
}