package com.example.mealprep.fill.out.recipe.card.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.mealprep.fill.out.recipe.card.TabScreen
import com.example.mealprep.fill.out.recipe.card.TopBarRecipeCreationForm
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeCreationScreen(
    navController: NavHostController,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    viewModal: RecipeCreationViewModel,
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = { TopBarRecipeCreationForm(navController, viewModal) },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    TabScreen(viewModal)
                }
            },
            backgroundColor = Color.White,
        )
    }
}