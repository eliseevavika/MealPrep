package com.example.mealprep.fill.out.recipe.card

import androidx.compose.runtime.Composable

@Composable
fun IngredientsCreationScreen() {
    ListIngredientsUpperPart()
    SearchScreen(viewModel = SearchViewModel())
}