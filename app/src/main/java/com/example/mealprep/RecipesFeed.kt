package com.example.mealprep

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.Dish
import com.example.littlelemon.DishDetails
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.meaprep.R

@Composable
fun RecipesFeed(navController: NavHostController, dishes: List<Dish> = listOf()) {
    Column {
//        WeeklySpecialCard()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            itemsIndexed(dishes) { _, dish ->
                MenuDish(navController, dish)
            }
        }
    }
}

//@Composable
//fun WeeklySpecialCard() {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        Text(
//            text = "Weekly Special",
//            style = MaterialTheme.typography.h1,
//            modifier = Modifier
//                .padding(8.dp)
//        )
//    }
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuDish(navController: NavHostController? = null, dish: Dish) {
    Card(
        modifier = Modifier
//            .background(Color.Red)
            .padding(8.dp)
            .wrapContentSize(),
        onClick = {
            Log.d("AAA", "Click ${dish.id}")
            navController?.navigate(DishDetails.route + "/${dish.id}")
        }) {

        Row(
            Modifier

//                .background(Color.Blue)
//            modifier = Modifier
//                .width(144.dp)
//                .fillMaxWidth()
//                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier
//                    .background(Color.Green)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,

            ) {
                Image(
                    painter = painterResource(id = dish.imageResource),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
//                        .background(Color.Yellow)
                        .size(144.dp, 171.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                )

                Text(
                    text = dish.name.addEmptyLines(2),
                    maxLines = 2,
                    style = MaterialTheme.typography.body1,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons_clock2),
                        contentDescription = "Clock icon",
                        Modifier
                            .size(24.dp)
                            .align(CenterVertically),
                        tint = MealPrepColor.grey_800
                    )
                    Column() {
                        Text(
                            text = "Prep: ${dish.prepTime}",
                            style = MaterialTheme.typography.body2,
                            color = MealPrepColor.grey_800,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Cook: ${dish.cookTimeTime}",
                            style = MaterialTheme.typography.body2,
                            color = MealPrepColor.grey_800,
                            fontSize = 14.sp
                        )
                    }
                }


            }


        }
    }


//    Divider(
//        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
//        thickness = 1.dp,
//        color = MealPrepColor.yellow
//    )
}

fun String.addEmptyLines(lines: Int) = this + "\n".repeat(lines)