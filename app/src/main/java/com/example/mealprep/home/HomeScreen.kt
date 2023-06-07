package com.example.littlelemon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: RecipeCreationViewModel
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
                val recipeList: List<Recipe> by viewModel.allRecipes.observeAsState(initial = listOf())
                Column {
                    RecipesFeed(
                        navController,
                        recipeList,
                        false,
                        viewModel,
                        -1
                    )
                }
            }
        }
    )
}