package com.example.littlelemon

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mealprep.RecipesFeed
import com.example.mealprep.TopAppBarHome

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBarHome()
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column {

//        UpperPanel()
                    RecipesFeed(navController, DishRepository.dishes)
                }

            }
        }
    )
}