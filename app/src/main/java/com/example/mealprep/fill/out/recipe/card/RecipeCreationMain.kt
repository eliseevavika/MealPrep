package com.example.mealprep.fill.out.recipe.card

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.littlelemon.DishDetails
import com.example.littlelemon.HomeScreen
import com.example.mealprep.*

class RecipeCreationMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Scaffold(
                topBar = { TopAppBar() },
                content = { padding -> //
                    Card(modifier = Modifier.fillMaxWidth(),  elevation = 0.dp ) {
                        NavigationBarCreation(navController = navController)
                    }
                    Box(modifier = Modifier.padding(padding)) {
                        NavHost(
                            navController = navController,
                            startDestination = NavigationItemCreation.Intro.route
                        ) {
                            composable(NavigationItemCreation.Intro.route) {
                               IntroCreationScreen(navController)
                            }
                            composable(NavigationItemCreation.Ingredients.route) {
//                                HomeScreen(navController)
                            }
                            composable(NavigationItemCreation.Steps.route) {
//                                HomeScreen(navController)
                            }
                        }
                    }
                },

                backgroundColor = Color.White,

                )




        }
        }
    }





