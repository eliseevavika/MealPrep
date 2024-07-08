package com.sliceup.mealprep.ui.mealplanning

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sliceup.mealprep.data.model.Day
import com.sliceup.mealprep.data.model.days
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.ui.navigation.BottomNavigationBar
import com.sliceup.mealprep.ui.navigation.DishDetails
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.data.Recipe
import com.sliceup.mealprep.ui.home.addEmptyLines
import com.sliceup.mealprep.ui.navigation.MealPrepForSpecificDay
import com.sliceup.mealprep.R

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealPlanningScreen(
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel,
) {
    val context = LocalContext.current
    val chosenDay by rememberUpdatedState(viewModel().chosenDay.collectAsState()).value
    val recipesForSunday by viewModel().recipesForSunday.collectAsState(listOf())

    val recipesForMonday by viewModel().recipesForMonday.collectAsState(
        listOf()
    )

    val recipesForTuesday by viewModel().recipesForTuesday.collectAsState(
        listOf()
    )

    val recipesForWednesday by viewModel().recipesForWednesday.collectAsState(
        listOf()
    )

    val recipesForThursday by viewModel().recipesForThursday.collectAsState(
        listOf()
    )

    val recipesForFriday by viewModel().recipesForFriday.collectAsState(
        listOf()
    )

    val recipesForSaturday by viewModel().recipesForSaturday.collectAsState(
        listOf()
    )

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
                TopAppBarMealPlanning(viewModel,
                    recipesForSunday,
                    recipesForMonday,
                    recipesForTuesday,
                    recipesForWednesday,
                    recipesForThursday,
                    recipesForFriday,
                    recipesForSaturday,
                    showMessage = {
                        android.app.AlertDialog.Builder(context)
                            .setMessage("It looks like there are no planned recipes for this week")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    })
            },
            bottomBar = { BottomNavigationBar(navController = navController) },
        ) { padding ->
            Box(
                modifier = Modifier.padding(
                    top = 16.dp, start = 16.dp, end = 16.dp, bottom = 35.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .verticalScroll(scrollState)
                ) {
                    days.forEach { day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp, top = 30.dp, bottom = 30.dp
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                ShowIcon()
                                Spacer(modifier = Modifier.width(width = 8.dp))
                                Text(
                                    text = day.title,
                                    fontFamily = fontFamilyForBodyB2,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        if (recipesForSunday.isNotEmpty() && day.id == 0) {
                            MealPlanRecipesByDay({ recipesForSunday }, navController)
                        } else if (recipesForMonday.isNotEmpty() && day.id == 1) {
                            MealPlanRecipesByDay({ recipesForMonday }, navController)
                        } else if (recipesForTuesday.isNotEmpty() && day.id == 2) {
                            MealPlanRecipesByDay({ recipesForTuesday }, navController)
                        } else if (recipesForWednesday.isNotEmpty() && day.id == 3) {
                            MealPlanRecipesByDay({ recipesForWednesday }, navController)
                        } else if (recipesForThursday.isNotEmpty() && day.id == 4) {
                            MealPlanRecipesByDay({ recipesForThursday }, navController)
                        } else if (recipesForFriday.isNotEmpty() && day.id == 5) {
                            MealPlanRecipesByDay({ recipesForFriday }, navController)
                        } else if (recipesForSaturday.isNotEmpty() && day.id == 6) {
                            MealPlanRecipesByDay({ recipesForSaturday }, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    navController: () -> NavHostController, viewModel: () -> RecipeViewModel, chosenDay: Day
) {
    Column {
        BottomSheetListItem(icon = R.drawable.outline_edit_24,
            title = "Add / Edit plan for ${chosenDay.title}",
            onItemClick = {
                viewModel().performQueryForChosenMealsFromDB(chosenDay.id)
                navController().navigate(MealPrepForSpecificDay.route + "/${chosenDay.id}")
            })
        BottomSheetListItem(
            icon = R.drawable.outline_delete_24,
            title = "Reset menu",
            onItemClick = {
                viewModel().deleteAllRecipesForDay(chosenDay.id)
            })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealPlanRecipesByDay(recipes: () -> List<Recipe>, navController: () -> NavHostController) {
    LazyRow(
        modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        items(recipes()) { recipe ->
            val recipeName = remember(recipe.recipe_id) { recipe.name.addEmptyLines(2) }

            val imagePathFromDatabase = recipe.photo

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(imagePathFromDatabase)
                    .size(Size.ORIGINAL).build()
            )
            Card(modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
                onClick = {
                    navController().navigate(DishDetails.route + "/${recipe.recipe_id}" + "/${true}")
                }) {
                Row {
                    Column(
                        modifier = Modifier.size(74.dp, 108.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (imagePathFromDatabase != "") {
                            Image(
                                painter = painter,
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