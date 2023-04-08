package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.NoRippleInteractionSource
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2

@Composable
fun TabScreen() {
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
//                var ofsetX = if(index != 1)  -45f else  -35f
//                var ofsetY = if(index != 1)  -5f else  -5f
                Tab(
                    text = {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp,
                            color = if (tabIndex == index) MealPrepColor.white else MealPrepColor.grey_800,

                            modifier = Modifier
                                .drawBehind {
                                    drawRoundRect(
                                        color = if (tabIndex == index) MealPrepColor.orange else MealPrepColor.transparent,
                                        cornerRadius = CornerRadius(x = 100f, y = 100f),
//                                    topLeft = Offset(
////                                        x = -25f,
////                                        y = -5f
//                                        x = ofsetX,
//                                        y = ofsetY
//                                    ),
//                                    size = Size(width = withForTab.toPx(), height = 36 .dp.toPx())
                                    )
                                }
//                            .size(width = withForTab, height = 36.dp)
                                .padding(
                                    start = (tabs[1].length - tabs[index].length).dp.plus(2.dp),
                                    end = (tabs[1].length - tabs[index].length).dp.plus(2.dp),
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
            0 -> IntroCreationScreen()
            1 -> IngredientsCreationScreen()
            2 -> StepsCreationScreen()
        }
    }
}