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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R
import java.io.File


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecipesFeed(
    navController: NavHostController,
    recipes: List<Recipe> = listOf(),
    isMealPlanningOn: Boolean,
    viewModel: RecipeCreationViewModel
) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            itemsIndexed(recipes) { _, recipe ->
                MenuDish(navController, recipe, isMealPlanningOn, viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(
    navController: NavHostController? = null,
    recipe: Recipe,
    isMealPlanningOn: Boolean,
    viewModel: RecipeCreationViewModel
) {
    val chosenDishesForMealPrep = viewModel.listChosenMeals.observeAsState().value

    val photoStrState = viewModel.photo.collectAsState()

    val context = LocalContext.current

    val cookTimeString = viewModel.getCookTimeString(recipe.cook_time)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        onClick = {
            if (!isMealPlanningOn) {
                navController?.navigate(DishDetails.route + "/${recipe.recipe_id}")
            } else {
                viewModel.performQueryForChosenMeals(recipe)
            }
        }) {
        var bitmap = recipe.photo?.let { Converters().converterStringToBitmap(it) }

        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
            ) {
                if (bitmap != null) {
                    bitmap?.asImageBitmap()?.let {
                        val mediaStorageDir: File? = context.getExternalFilesDir(null)
                        val fileName =
                            photoStrState.value.substring(photoStrState.value.lastIndexOf('/') + 1)

                        val dir = File(mediaStorageDir?.path)

                        viewModel.saveImage(bitmap, dir, fileName)

                        Converters().converterStringToBitmap(recipe.photo.toString())?.let { it1 ->
                            Image(
                                bitmap = it1.asImageBitmap(),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(144.dp, 171.dp)
                                    .clip(
                                        RoundedCornerShape(16.dp)
                                    )
                                    .alpha(
                                        if (isMealPlanningOn && chosenDishesForMealPrep?.contains(
                                                recipe
                                            ) == true
                                        ) 0.2F else 1F
                                    )
                            )
                        }
                    }
                } else {
                    Image(
                        painterResource(id = R.drawable.noimage),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(144.dp, 171.dp)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .alpha(
                                if (isMealPlanningOn && chosenDishesForMealPrep?.contains(
                                        recipe
                                    ) == true
                                ) 0.2F else 1F
                            )
                    )
                }
                Text(
                    text = recipe.name.addEmptyLines(2),
                    maxLines = 2,
                    fontSize = 20.sp,
                    fontFamily = fontFamilyForBodyB1,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons_clock2),
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
        }
    }
}

fun String.addEmptyLines(lines: Int) = this + "\n".repeat(lines)

