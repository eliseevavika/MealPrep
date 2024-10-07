package com.sliceuptest.mealprep.ui.creation

import androidx.compose.runtime.Composable
import com.sliceuptest.mealprep.viewmodel.RecipeViewModel

@Composable
fun StepsCreationScreen(viewModel: () -> RecipeViewModel) {
    ListStepsScreen(viewModel)
}