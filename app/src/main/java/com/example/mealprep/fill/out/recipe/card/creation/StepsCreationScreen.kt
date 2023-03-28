package com.example.mealprep.fill.out.recipe.card

import androidx.compose.runtime.Composable
import com.example.mealprep.fill.out.recipe.card.creation.StepsCreationViewModel

@Composable
fun StepsCreationScreen() {
    ListStepsScreen(viewModel = StepsCreationViewModel())
}