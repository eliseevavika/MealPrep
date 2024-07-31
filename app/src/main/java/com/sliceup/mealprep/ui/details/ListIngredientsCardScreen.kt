package com.sliceup.mealprep.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceup.mealprep.data.Ingredient
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.R


@OptIn(ExperimentalUnitApi::class)
@Composable
fun ListIngredientsCardScreen(
    viewModel: () -> RecipeViewModel
) {
    val ingredientsList = viewModel().returnedListIngredient.observeAsState().value

    Box(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            if (!ingredientsList.isNullOrEmpty()) {
                items(ingredientsList) { item ->
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .fillParentMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        setUpRow(item)
                    }
                }
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun setUpRow(
    ingredient: Ingredient
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 10.dp, top = 10.dp, end = 16.dp, bottom = 16.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(7f), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_brightness_1_24),
                tint = MealPrepColor.orange,
                contentDescription = "Icon Check",
                modifier = Modifier
                    .size(16.dp)
                    .alignBy { 0 }
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = ingredient.name, fontFamily = fontFamilyForBodyB2,
                fontSize = 16.sp,
                modifier = Modifier.alignBy(FirstBaseline)
            )
        }
    }
}