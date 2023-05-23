package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.NoRippleInteractionSource
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2

@Composable
fun TabScreenForRecipeCard(viewModel: RecipeCreationViewModel) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Intro", "Ingredients", "Steps")


    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = MealPrepColor.white,
            indicator = {},
        ) {
            tabs.forEachIndexed { index, title ->
                var withForTab = if (index != 1) 94.5.dp else 120.dp

                Tab(
                    text = {
                        Text(
                            text = title,
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp,
                            color = if (tabIndex == index) MealPrepColor.white else MealPrepColor.grey_800,

                            modifier = Modifier
                                .drawBehind {
                                    drawRoundRect(
                                        color = if (tabIndex == index) MealPrepColor.orange else MealPrepColor.transparent,
                                        cornerRadius = CornerRadius(x = 100f, y = 100f),
                                    )
                                }
                                .size(width = withForTab, height = 36.dp)
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp
                                )
                        )
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    interactionSource = NoRippleInteractionSource()
                )
            }
        }
        when (tabIndex) {
            0 -> IntroCardScreen(viewModel)
            1 -> ListIngredientsCardScreen(viewModel)
            2 -> StepsCardScreen(viewModel)
        }
    }
}