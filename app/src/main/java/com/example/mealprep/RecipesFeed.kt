package com.example.mealprep

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.Dish
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.meaprep.R

@Composable
fun RecipesFeed(
    navController: NavHostController,
    dishes: List<Dish> = listOf(),
    isMealPlanningOn: Boolean,
    viewModel: MealPlanningViewModel
) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            itemsIndexed(dishes) { _, dish ->
                MenuDish(navController, dish, isMealPlanningOn, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuDish(
    navController: NavHostController? = null,
    dish: Dish,
    isMealPlanningOn: Boolean,
    viewModel: MealPlanningViewModel
) {
    val chosenDishesForMealPrep = viewModel.list.observeAsState().value

    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        onClick = {
            if (!isMealPlanningOn) {
                navController?.navigate(DishDetails.route + "/${dish.id}")
            } else {
                viewModel.performQuery(dish)
            }
        }) {

        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
                ) {
                Image(
                    painter = painterResource(id = dish.imageResource),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(144.dp, 171.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .alpha(if (isMealPlanningOn && chosenDishesForMealPrep?.contains(dish) == true) 0.2F else 1F)
                )

                Text(
                    text = dish.name.addEmptyLines(2),
                    maxLines = 2,
                    style = MaterialTheme.typography.body1,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons_clock2),
                        contentDescription = "Clock icon",
                        Modifier
                            .size(24.dp)
                            .align(CenterVertically),
                        tint = MealPrepColor.grey_800
                    )
                    Column() {
                        Text(
                            text = "Prep: ${dish.prepTime}",
                            style = MaterialTheme.typography.body2,
                            color = MealPrepColor.grey_800,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Cook: ${dish.cookTimeTime}",
                            style = MaterialTheme.typography.body2,
                            color = MealPrepColor.grey_800,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

fun String.addEmptyLines(lines: Int) = this + "\n".repeat(lines)