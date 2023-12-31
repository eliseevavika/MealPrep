package com.example.mealprep.ui.creation

import androidx.compose.runtime.Composable
import com.example.mealprep.viewmodel.RecipeViewModel

@Composable
fun StepsCreationScreen(viewModel: () -> RecipeViewModel) {
    ListStepsScreen(viewModel)
}