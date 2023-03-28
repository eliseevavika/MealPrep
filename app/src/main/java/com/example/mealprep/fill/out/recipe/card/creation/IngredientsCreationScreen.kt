package com.example.mealprep.fill.out.recipe.card

import androidx.compose.runtime.Composable
import com.example.mealprep.fill.out.recipe.card.creation.ListIngredientsCreationViewModel

@Composable
fun IngredientsCreationScreen() {
    ListIngredientsCreationScreen(viewModel = ListIngredientsCreationViewModel())
}