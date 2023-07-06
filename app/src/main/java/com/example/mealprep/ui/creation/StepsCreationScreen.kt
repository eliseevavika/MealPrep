package com.example.mealprep.fill.out.recipe.card

import androidx.compose.runtime.Composable
import com.example.mealprep.viewmodel.RecipeViewModel

@Composable
fun StepsCreationScreen(viewModel: () -> RecipeViewModel) {
    ListStepsScreen(viewModel)
}