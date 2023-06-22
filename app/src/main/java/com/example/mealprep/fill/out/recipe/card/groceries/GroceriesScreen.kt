package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealprep.BottomNavigationBar
import com.example.mealprep.GroceriesAddition
import com.example.mealprep.Ingredient
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2


@OptIn(ExperimentalUnitApi::class)
@Composable
fun GroceriesScreen(navController: NavHostController, viewModel: () -> RecipeCreationViewModel) {
    Scaffold(topBar = {
        TopBarForGroceriesScreen()
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate(GroceriesAddition.route)
            },
            modifier = Modifier
                .padding(all = 16.dp),
            backgroundColor = MealPrepColor.orange,
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = MealPrepColor.white
            )
        }
    },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {
                val listGroceries =
                    viewModel().ingredientsFromMealPlans.observeAsState(listOf()).value

                Column(
                    Modifier.wrapContentHeight()
                ) {
                    var expand by remember { mutableStateOf(false) }
                    val completedIngredients =
                        viewModel().completedIngredients.observeAsState().value

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (listGroceries.isNotEmpty()) {
                            listGroceries.forEach { item ->
                                key(item.id) {
                                    Column(
                                        modifier = Modifier
                                            .background(Color.White),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        setUpLines(item, viewModel, false, completedIngredients)
                                    }
                                    if (listGroceries.last() == item) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = 20.dp,
                                                    start = 8.dp,
                                                    end = 8.dp,
                                                    bottom = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Completed",
                                                color = MealPrepColor.orange,
                                                fontSize = 20.sp,
                                                textAlign = TextAlign.Start,
                                                fontWeight = FontWeight.Normal,
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                            )
                                            IconButton(
                                                modifier = Modifier.rotate(if (expand) 180F else 0F),
                                                onClick = {
                                                    expand = !expand
                                                }) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowDown,
                                                    tint = MealPrepColor.orange,
                                                    contentDescription = "Drop Down Arrow"
                                                )
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        if (expand) {
                            if (!completedIngredients.isNullOrEmpty()) {
                                completedIngredients.forEach { item ->
                                    key(item.id) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            setUpLines(item, viewModel, true, completedIngredients)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
}


@ExperimentalUnitApi
@Composable
fun setUpLines(
    item: Ingredient,
    viewModel: () -> RecipeCreationViewModel,
    isCompleted: Boolean,
    completedIngredients: List<Ingredient>?
) {
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
                .weight(7f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = completedIngredients?.contains(item) ?: false,
                modifier = Modifier.size(16.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MealPrepColor.orange, unselectedColor = MealPrepColor.black
                ),
                onClick = {
                    viewModel().performQueryForGroceries(item)
                },
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = item.name,
                fontFamily = fontFamilyForBodyB2,
                style = if (isCompleted) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(
                    textDecoration = TextDecoration.None
                ),
                fontSize = 16.sp
            )
        }
    }
}