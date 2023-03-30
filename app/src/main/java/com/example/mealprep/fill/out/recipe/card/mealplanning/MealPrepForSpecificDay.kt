package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.Dish
import com.example.littlelemon.DishRepository
import com.example.littlelemon.HomeScreen
import com.example.mealprep.MyFloatingActionButton
import com.example.mealprep.RecipesFeed
import com.example.mealprep.TopAppBarHome

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MealPrepForSpecificDay(dayId: Int, navController: NavHostController) {
    val dishes = remember { mutableStateOf(DishRepository.dishes) }

    var filteredDishes = remember { mutableStateOf(DishRepository.dishes) }

    Scaffold(
        topBar = {
            TopAppBarMealbyDays()
        },

        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column {


                    Column {
                        Spacer(modifier = Modifier.height(20.dp))

                        SearchBar(onSearch = {
                            val result = dishes.value.filter { dish ->
                                dish.name.lowercase().contains(it.lowercase())
                            }

                            if (result.isNotEmpty()) {
                                filteredDishes.value = result.toMutableStateList()
                            } else {
                                filteredDishes.value = mutableListOf<Dish>()
                            }
                        })

//                            Spacer(modifier = Modifier.height(10.dp))

                        if (filteredDishes.value.size == 0) {
                            RecipesFeed(navController, DishRepository.dishes)
                        }

//                        filteredDishes.value.map {
//                            RecipesFeed(navController, DishRepository.dishes)
                        RecipesFeed(navController, filteredDishes.value)
//                        }
                    }
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

    Column() {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(text = "Recipe title, Ingredient... ") },
                modifier = Modifier.focusable(true),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") }
            )

            Spacer(modifier = Modifier.width(10.dp))

            onSearch(searchQuery)
        }
    }
}


