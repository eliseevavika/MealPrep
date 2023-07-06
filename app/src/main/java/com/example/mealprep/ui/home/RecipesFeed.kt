package com.example.mealprep

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mealprep.ui.navigation.DishDetails
import com.example.mealprep.viewmodel.RecipeViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecipesFeed(
    navController: () -> NavHostController,
    recipes: () -> List<Recipe> = { listOf() },
    isMealPlanningOn: Boolean,
    viewModel: () -> RecipeViewModel,
    dayId: Int
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        itemsIndexed(recipes()) { _, recipe ->
            MenuDish(
                navController,
                recipe,
                isMealPlanningOn,
                viewModel(),
                dayId
            ) {
                viewModel().performQueryForChosenMeals(it, dayId)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuDish(
    navController: (() -> NavHostController),
    recipe: Recipe,
    isMealPlanningOn: Boolean,
    viewModel: RecipeViewModel,
    dayId: Int,
    onPerformQuery: (Recipe) -> Unit,
) {
    val cookTimeString = viewModel.getCookTimeString(recipe.cook_time)

    val recipeName = remember(recipe.recipe_id) { recipe.name.addEmptyLines(2) }

    val imagePathFromDatabase = recipe.photo

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePathFromDatabase)
            .size(coil.size.Size.ORIGINAL)
            .build()
    )
    Card(modifier = Modifier
        .padding(8.dp)
        .wrapContentSize(), onClick = {
        if (!isMealPlanningOn) {
            navController().navigate(DishDetails.route + "/${recipe.recipe_id}")
        } else {
            onPerformQuery(recipe)
        }
    }) {
        val chosenMealsForThisDay = if (dayId == 0) {
            viewModel.listChosenMealsForSunday.collectAsState().value
        } else if (dayId == 1) {
            viewModel.listChosenMealsForMonday.collectAsState().value
        } else if (dayId == 2) {
            viewModel.listChosenMealsForTuesday.collectAsState().value
        } else if (dayId == 3) {
            viewModel.listChosenMealsForWednesday.collectAsState().value
        } else if (dayId == 4) {
            viewModel.listChosenMealsForThursday.collectAsState().value
        } else if (dayId == 5) {
            viewModel.listChosenMealsForFriday.collectAsState().value
        } else if (dayId == 6) {
            viewModel.listChosenMealsForSaturday.collectAsState().value
        } else {
            emptyList()
        }

        val alpha = remember(recipe, dayId, chosenMealsForThisDay) {
            if (isMealPlanningOn && chosenMealsForThisDay.contains(recipe)) {
                0.2f
            } else {
                1f
            }
        }

        Row {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
            ) {
                if(imagePathFromDatabase != null){
                    Image(
                        painter = painter,
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(144.dp, 171.dp)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .alpha(alpha)
                    )
                }else{
                    ShowDefaultImage(alpha)
                }

                Text(
                    text = recipeName,
                    maxLines = 2,
                    fontSize = 20.sp,
                    fontFamily = fontFamilyForBodyB1,
                    modifier = Modifier.padding(start = 16.dp)
                )
                CooktimeIconAndTitle(cookTimeString)
            }
        }
    }
}

@Composable
fun CooktimeIconAndTitle(cookTimeString: String) {
    val clockIconPainter = painterResource(id = R.drawable.icons_clock2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            painter = clockIconPainter,
            contentDescription = "Clock icon",
            Modifier
                .size(24.dp)
                .align(CenterVertically),
            tint = MealPrepColor.grey_800
        )

        Text(
            text = "Cook: $cookTimeString",
            fontFamily = fontFamilyForBodyB2,
            color = MealPrepColor.grey_800,
            fontSize = 14.sp
        )
    }
}

fun String.addEmptyLines(lines: Int) = this + "\n".repeat(lines)

@Composable
fun ShowDefaultImage(alpha: Float) {
    Image(
        painterResource(id = R.drawable.noimage),
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(144.dp, 171.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .alpha(alpha)
    )
}