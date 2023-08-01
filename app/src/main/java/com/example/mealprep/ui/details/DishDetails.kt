package com.example.littlelemon

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.mealprep.fill.out.recipe.card.TabScreenForRecipeCard
import com.example.mealprep.viewmodel.RecipeViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R

@Composable
fun TopAppBarDishDetail(
    navController: () -> NavHostController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .fillMaxWidth(60f)
            .background(MealPrepColor.transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            navController().popBackStack("home", inclusive = false)
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DishDetails(
    id: Long, navController: () -> NavHostController, viewModel: () -> RecipeViewModel
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
        TopAppBarDishDetail(navController)
    },
        content = { padding ->
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
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePathFromDatabase)
            .size(Size.ORIGINAL)
            .build()
    )
    val cookTimeString = remember(recipe){
        viewModel().getCookTimeString(recipe?.cook_time)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy((-150).dp),
        modifier = Modifier
            .background(MealPrepColor.grey_100)
            .height(300.dp)
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
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F)
                        .padding(start = 16.dp, end = 16.dp), verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = "${recipe?.name}",
                        fontFamily = fontFamilyForBodyB1,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxHeight(0.8F)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
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
                                }, fontFamily = fontFamilyForBodyB2, fontSize = 16.sp
                            )
                        }
                    }
                    Divider(
                        color = MealPrepColor.grey_400,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
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
                                fontSize = 16.sp
                            )
                        }
                    }
                    Divider(
                        color = MealPrepColor.grey_400,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_restaurant_24),
                                tint = MealPrepColor.orange,
                                contentDescription = "Cooking time",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = "Serves: " + (recipe?.serves?.toString() ?: "0"),
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp
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