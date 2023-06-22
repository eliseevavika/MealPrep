package com.example.mealprep.fill.out.recipe.card.mealplanning

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.Day
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.fill.out.recipe.card.days
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealPlanningScreen(
    navController: NavHostController,
    viewModel: () -> RecipeCreationViewModel,
) {
    val recipesByDay = remember(viewModel().recipesByDay) { viewModel().recipesByDay }

    val chosenDay by rememberUpdatedState(viewModel().chosenDay.collectAsState()).value

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    val isSheetFullScreen by remember { mutableStateOf(false) }
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp
    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize()
    else Modifier.fillMaxWidth()
    val scrollState = rememberScrollState()

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheetLayout(sheetState = modalSheetState, sheetShape = RoundedCornerShape(
        topStart = roundedCornerRadius, topEnd = roundedCornerRadius
    ), sheetContent = {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BottomSheetContent(navController, viewModel, days[chosenDay])
        }
    }) {
        Scaffold(
            topBar = {
                TopAppBarMealPlanning()
            },
            bottomBar = { BottomNavigationBar(navController = navController) },

            ) { padding ->

            Box(modifier = Modifier.padding(16.dp)) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .verticalScroll(scrollState)
                ) {
                    days.forEach { day ->
                        key(day.id) {
                            val recipesForDay =
                                recipesByDay.getOrNull(day.id)?.collectAsState(initial = listOf())?.value

                            Row(
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp, top = 30.dp, end = 16.dp, bottom = 30.dp
                                    )
                                    .clickable(onClick = {
                                        coroutineScope.launch {
                                            if (modalSheetState.isVisible) {
                                                modalSheetState.hide()
                                            } else {
                                                viewModel().setChosenDay(day.id)
                                                modalSheetState.show()
                                            }
                                        }
                                    })
                            ) {
                                Row {
                                    //extracted composable to skip recomposition
                                    ShowIcon()
                                    Spacer(modifier = Modifier.width(width = 8.dp))
                                    Text(
                                        text = day.title,
                                        fontFamily = fontFamilyForBodyB2,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            if (recipesForDay != null && recipesForDay.isNotEmpty()) {
                                MealPlanRecipesByDay { recipesForDay }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    navController: NavHostController, viewModel: () -> RecipeCreationViewModel, chosenDay: Day
) {
    val rememberedDay = remember(chosenDay) { chosenDay }

    Column {
        BottomSheetListItem(icon = R.drawable.outline_edit_24,
            title = "Add / Edit plan for ${rememberedDay.title}",
            onItemClick = {
                viewModel().performQueryForChosenMealsFromDB(rememberedDay.id)
                navController.navigate(MealPrepForSpecificDay.route + "/${rememberedDay.id}")
            })
        BottomSheetListItem(icon = R.drawable.outline_delete_24,
            title = "Reset menu",
            onItemClick = {
                viewModel().deleteAllRecipesForDay(rememberedDay.id)
            })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealPlanRecipesByDay(recipes: () -> List<Recipe>) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .padding(start = 8.dp, bottom = 16.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.Start
    ) {
        recipes().forEach { recipe ->
            val recipeName = remember(recipe.recipe_id){recipe.name.addEmptyLines(2)}
            key(recipe.recipe_id) {
                val bitmap = recipe.photo?.let {
                    Converters().converterStringToBitmap(it)
                }
                Card(modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(), onClick = {}) {
                    Row {
                        Column(
                            modifier = Modifier.size(74.dp, 108.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,

                            ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(
                                            74.dp, 74.dp
                                        )
                                        .clip(
                                            RoundedCornerShape(
                                                16.dp
                                            )
                                        )
                                )
                            } else {
                                ShowDefaultIconForRecipe()
                            }
                            Text(
                                text = recipeName,
                                maxLines = 2,
                                fontFamily = fontFamilyForBodyB1,
                                fontSize = 10.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(title) })
            .height(55.dp)
            .background(color = colorResource(id = R.color.white))
            .padding(start = 15.dp, top = 15.dp, bottom = 5.dp),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = MealPrepColor.grey_600
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = title,
                color = Color.Black,
                fontFamily = fontFamilyForBodyB2,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ShowIcon() {
    Icon(
        painter = painterResource(id = R.drawable.outline_density_medium_24),
        tint = MealPrepColor.grey_600,
        contentDescription = "Icon",
        modifier = Modifier.size(16.dp)
    )
}

@Composable
fun ShowDefaultIconForRecipe() {
    Image(
        painterResource(id = R.drawable.noimage),
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(74.dp, 74.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
    )
}