package com.example.mealprep.fill.out.recipe.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.DishRepository
import com.example.mealprep.BottomNavigationBar
import com.example.mealprep.ExpandableCard
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2


@OptIn(ExperimentalUnitApi::class)
@Composable
fun GroceriesScreen(navController: NavHostController, viewModel: MealPlanningViewModel,) {
    Scaffold(
        topBar = {
            TopBarForGroceriesScreen()
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            Box(modifier = Modifier.padding(top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp)) {
                val dish = requireNotNull(DishRepository.getDish(1))

                val listIngredients = dish.ingredientsList

                val chosenGroceries = viewModel.chosenGroceries.observeAsState().value

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
                                setUpLines(item, viewModel)
                            }
                        }
                    }
                }

                    ExpandableCard("Completed",viewModel, MealPrepColor.orange)



            }

        }
    )

}

@ExperimentalUnitApi
@Composable
fun setUpLines(
    item: String,
    viewModel: MealPlanningViewModel
) {

    val chosenGroceries = viewModel.chosenGroceries.observeAsState().value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 10.dp, top = 10.dp, end = 16.dp, bottom = 30.dp),

        ) {

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(7f), verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = chosenGroceries?.contains(item) ?: false,
                modifier = Modifier.size(16.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MealPrepColor.orange,
                    unselectedColor = MealPrepColor.black
                ),
                onClick = {
                    viewModel.performQueryForGroceries(item)

                },
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = item, fontFamily = fontFamilyForBodyB2,
                fontSize = 16.sp
            )
        }
    }
}

//@OptIn(ExperimentalUnitApi::class)
//@Composable
//fun showCollapsibleItems(chosenGroceries: List<String>, viewModel: MealPlanningViewModel) {
//    LazyColumn {
//        items(chosenGroceries) { item ->
//            Column(
//                modifier = Modifier
//                    .background(Color.White)
//                    .fillParentMaxWidth(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                setUpLines(item, viewModel)
//            }
//        }
//    }
//}

