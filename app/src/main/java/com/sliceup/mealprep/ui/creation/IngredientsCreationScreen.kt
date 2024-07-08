package com.sliceup.mealprep.ui.creation

import androidx.compose.runtime.Composable
import com.sliceup.mealprep.viewmodel.RecipeViewModel

@Composable
fun IngredientsCreationScreen(viewModel: () -> RecipeViewModel) {
    ListIngredientsCreationScreen(viewModel)
}