package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.DishRepository
import com.example.mealprep.MealPrep
import com.example.mealprep.RecipesFeed
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2


@Composable
fun MealPrepForSpecificDay(
    dayId: Int,
    navController: NavHostController,
    viewModel: MealPlanningViewModel
) {
    val dishes = remember { mutableStateOf(DishRepository.dishes) }

    var filteredDishes = remember { mutableStateOf(DishRepository.dishes) }

    Scaffold(
        topBar = {
            TopAppBarMealbyDays()
        },
        floatingActionButton = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = CenterHorizontally
            ) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = "Edit",
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        navController.navigate(MealPrep.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.80F)
                        .padding(start = 30.dp),
                    backgroundColor = MealPrepColor.orange,
                    contentColor = Color.White,
                    icon = { }
                )
            }
        },

        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {

                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    SearchBar(onSearch = {
                        val result = dishes.value.filter { dish ->
                            dish.name.lowercase().contains(it.lowercase())
                        }
                        if (result.isNotEmpty()) {
                            filteredDishes.value = result.toMutableStateList()
                        } else {
                            filteredDishes.value = mutableListOf()
                        }
                    })
                    RecipesFeed(navController, filteredDishes.value, true, viewModel)
                }
            }
        }
    )
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Recipe title, Ingredient... ",
                        fontFamily = fontFamilyForBodyB2,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.focusable(true),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MealPrepColor.orange
                ),
                maxLines = 1,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") }
            )

            Spacer(modifier = Modifier.width(10.dp))

            onSearch(searchQuery)
        }
    }
}