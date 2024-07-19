package com.sliceup.mealprep.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceup.mealprep.ui.modifiers.NoRippleInteractionSource
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.viewmodel.RecipeViewModel

@Composable
fun TabScreenForRecipeCard(viewModel: () -> RecipeViewModel) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Intro", "Ingredients", "Steps")

    val tabIndicator = @Composable { tabPositions: List<TabPosition> ->
        TabRowDefaults.Indicator(
            color = MealPrepColor.orange,
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[tabIndex])
                .height(2.dp)
                .background(color = MealPrepColor.orange, shape = RoundedCornerShape(50))
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = MealPrepColor.white,
            indicator = tabIndicator,
        ) {
            tabs.forEachIndexed { index, title ->
                val widthForTab = if (index != 1) 84.5.dp else 130.dp

                key(index) {
                    Tab(
                        text = {
                            Text(
                                text = title,
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                                color = MealPrepColor.grey_800,
                                maxLines = 2,
                                modifier = Modifier
                                    .width(widthForTab)
                                    .padding(
                                        top = 16.dp,
                                        bottom = 16.dp
                                    )
                            )
                        },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        interactionSource = NoRippleInteractionSource()
                    )
                }
            }
        }
        when (tabIndex) {
            0 -> IntroCardScreen(viewModel)
            1 -> ListIngredientsCardScreen(viewModel)
            2 -> StepsCardScreen(viewModel)
        }
    }
}