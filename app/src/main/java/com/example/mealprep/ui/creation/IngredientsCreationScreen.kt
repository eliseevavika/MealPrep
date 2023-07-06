package com.example.mealprep.fill.out.recipe.card

import androidx.compose.runtime.Composable
import com.example.mealprep.viewmodel.RecipeViewModel

@Composable
fun IngredientsCreationScreen(viewModel: () -> RecipeViewModel) {
    ListIngredientsCreationScreen(viewModel)
}