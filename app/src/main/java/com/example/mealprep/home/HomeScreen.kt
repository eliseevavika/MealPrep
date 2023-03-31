package com.example.littlelemon

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mealprep.BottomNavigationBar
import com.example.mealprep.MyFloatingActionButton
import com.example.mealprep.RecipesFeed
import com.example.mealprep.TopAppBarHome
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    Scaffold(
        topBar = {
            TopAppBarHome()
        },
        floatingActionButton = {
            MyFloatingActionButton(
                scope,
                modalBottomSheetState
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column {

//        UpperPanel()
                    RecipesFeed(
                        navController,
                        DishRepository.dishes,
                        false,
                        MealPlanningViewModel()
                    )
                }

            }
        }
    )
}