package com.sliceuptest.mealprep.ui.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sliceuptest.mealprep.viewmodel.RecipeViewModel
import com.sliceuptest.mealprep.ui.theme.MealPrepColor
import com.sliceuptest.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceuptest.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.R

@Composable
fun TopAppBarDishDetail(
    navController: () -> NavHostController, mealPrepOn: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .fillMaxWidth(0.6f)
            .background(MealPrepColor.transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (!mealPrepOn) {
                navController().popBackStack("home", inclusive = false)
            } else {
                navController().popBackStack("mealprep", inclusive = false)
            }
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DishDetails(
    id: Long,
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel,
    mealPrepOn: Boolean
) {
    Scaffold(topBar = {
        TopAppBarDishDetail(navController, mealPrepOn)
    }, content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column {
                viewModel().getRecipe(id)

                viewModel().getListOfIngredients(id)

                viewModel().getListOfSteps(id)

                UpperPart(viewModel)

                LowerPart(viewModel)
            }
        }
    })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpperPart(viewModel: () -> RecipeViewModel) {
    val recipe = viewModel().returnedRecipe.observeAsState().value

    val imagePathFromDatabase = recipe?.photo

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(imagePathFromDatabase)
            .size(Size.ORIGINAL).build()
    )
    val cookTimeString = remember(recipe) {
        viewModel().getCookTimeString(recipe?.cook_time)
    }
    var maxHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        verticalArrangement = Arrangement.spacedBy((-150).dp),
        modifier = Modifier
            .background(MealPrepColor.grey_100)
            .wrapContentHeight()
    ) {
        if (imagePathFromDatabase != "") {
            Image(
                painter = painter,
                contentDescription = "Dish image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } else {
            Image(
                painterResource(id = R.drawable.noimage),
                contentDescription = "Dish image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .align(CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .wrapContentHeight()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .wrapContentHeight(),
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = "${recipe?.name}",
                        fontFamily = fontFamilyForBodyB1,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                val height = with(density) { layoutCoordinates.size.height.toDp() }
                                if (height > maxHeight) maxHeight = height
                            }) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icons_clock2),
                                tint = MealPrepColor.orange,
                                contentDescription = "Cooking time",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = if (recipe?.cook_time == null) "0 min" else {
                                    cookTimeString
                                },
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                                modifier = Modifier.wrapContentHeight()
                            )
                        }
                    }
                    Divider(
                        color = MealPrepColor.grey_400,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .width(1.dp)
                            .height(maxHeight)
                    )
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_room_service_24),
                                tint = MealPrepColor.orange,
                                contentDescription = "Cooking time",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = viewModel().getCookingComplexity(recipe?.cook_time),
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                                modifier = Modifier.wrapContentHeight()
                            )
                        }
                    }
                    Divider(
                        color = MealPrepColor.grey_400,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .width(1.dp)
                            .height(maxHeight)
                    )
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_restaurant_24),
                                tint = MealPrepColor.orange,
                                contentDescription = "Cooking time",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = (recipe?.serves?.toString() ?: "0"),
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                                modifier = Modifier.wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LowerPart(viewModel: () -> RecipeViewModel) {
    TabScreenForRecipeCard(viewModel)
}