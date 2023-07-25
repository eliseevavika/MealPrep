package com.example.mealprep.ui.authentication

import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.example.mealprep.BackIcon
import com.example.mealprep.ui.theme.MealPrepColor

@Composable
fun SignUpTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
        },
        backgroundColor = MealPrepColor.transparent,
        contentColor = MealPrepColor.black,
        navigationIcon = {
            BackIcon(
                navigateBack = navigateBack
            )
        }
    )
}