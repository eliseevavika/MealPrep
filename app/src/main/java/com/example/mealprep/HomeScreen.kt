package com.example.littlelemon

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mealprep.RecipesFeed
import com.example.mealprep.TopAppBar

@Composable
fun HomeScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current.applicationContext


    Column {

//        UpperPanel()
        RecipesFeed(navController, DishRepository.dishes)
    }
}
//@Composable
//fun TopAppBar(scaffoldState: ScaffoldState? = null, scope: CoroutineScope? = null) {
//    Row(horizontalArrangement = Arrangement.Center,
//
//        verticalAlignment = Alignment.CenterVertically) {
//        Column() {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(Icons.Filled.ArrowBack, contentDescription = "Go back", Modifier.size(24.dp))
//            }
//        }
//
//    }
//}