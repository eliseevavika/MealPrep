package com.sliceup.mealprep.ui.creation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceup.mealprep.ui.modifiers.NoRippleInteractionSource
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabScreen(
    viewModal: () -> RecipeViewModel,
    focusRequester: FocusRequester
) {
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
                                    .padding(
                                        top = 16.dp,
                                        bottom = 16.dp
                                    )
                            )
                        },
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                            viewModal().setTabIndex(index)
                        }
                    )
                }
            }
        }
        when (tabIndex) {
            0 -> IntroCreationScreen(viewModal, focusRequester)
            1 -> IngredientsCreationScreen(viewModal)
            2 -> StepsCreationScreen(viewModal)
        }
    }
}