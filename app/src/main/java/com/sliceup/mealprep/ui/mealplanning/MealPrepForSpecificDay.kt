package com.sliceup.mealprep.ui.mealplanning

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sliceup.mealprep.ui.navigation.MealPrep
import com.sliceup.mealprep.data.Recipe
import com.sliceup.mealprep.ui.home.RecipesFeed
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealPrepForSpecificDay(
    dayId: Int,
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel
) {
    val dishes: List<Recipe> by viewModel().allRecipes.observeAsState(initial = listOf())

    var filteredDishes by remember { mutableStateOf(dishes) }

    Scaffold(topBar = {
        TopAppBarMealbyDays()
    }, floatingActionButton = {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = CenterHorizontally
        ) {
            ExtendedFloatingActionButton(text = {
                Text(
                    text = "Edit", fontFamily = fontFamilyForBodyB2, fontSize = 16.sp
                )
            },
                onClick = {
                    viewModel().addNewMealPlan()
                    navController().navigate(MealPrep.route)
                },
                modifier = Modifier
                    .fillMaxWidth(0.80F)
                    .padding(start = 30.dp),
                backgroundColor = MealPrepColor.orange,
                contentColor = Color.White,
                icon = { })
        }
    }, content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                SearchBar(onSearch = {
                    val result = dishes.filter { dish ->
                        dish.name.lowercase().contains(it.lowercase())
                    }
                    if (result.isNotEmpty()) {
                        filteredDishes = result.toMutableStateList()
                    } else {
                        filteredDishes = mutableListOf()
                    }
                })
                RecipesFeed(navController, { filteredDishes }, true, viewModel, dayId)
            }
        }
    })
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
            TextField(value = searchQuery,
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
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") })
            Spacer(modifier = Modifier.width(10.dp))
            onSearch(searchQuery)
        }
    }
}