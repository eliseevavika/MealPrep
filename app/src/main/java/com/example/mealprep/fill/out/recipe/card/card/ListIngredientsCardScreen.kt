package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlelemon.DishRepository
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R


//    val dish = requireNotNull(DishRepository.getDish(id))


@OptIn(ExperimentalUnitApi::class)
@Composable
fun ListIngredientsCardScreen(
    id: Int
) {
    Box(modifier = Modifier.padding(16.dp)) {
        val dish = requireNotNull(DishRepository.getDish(id))
        val listIngredients = dish.ingredientsList

        LazyColumn {
            if (!listIngredients.isNullOrEmpty()) {
                items(listIngredients) { item ->
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
    item: Groceries
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
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = item.name, fontFamily = fontFamilyForBodyB2,
                fontSize = 16.sp
            )
        }
    }
}


