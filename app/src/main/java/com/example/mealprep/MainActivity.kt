package com.example.mealprep

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.littlelemon.DishDetails
import com.example.littlelemon.HomeScreen
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningScreen
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPrepForSpecificDay
import com.example.mealprep.ui.theme.MealPrepTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealPrepTheme {
                val navController = rememberNavController()

                val modalBottomSheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                val scope = rememberCoroutineScope()

                BackHandler(modalBottomSheetState.isVisible) {
                    scope.launch { modalBottomSheetState.hide() }
                }



                ModalBottomSheetLayout(
                    sheetContent = {
                        BottomSheetContent(navController)
                    },
                    Modifier.fillMaxWidth(),
                    sheetState = modalBottomSheetState,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    sheetBackgroundColor = Color.Blue,
                ) {
                    Scaffold(
//                        topBar = { TopAppBar(navController) },
                        bottomBar = { BottomNavigationBar(navController = navController) },
//                        floatingActionButton = {
//                            MyFloatingActionButton(
//                                scope,
//                                modalBottomSheetState
//                            )
//                        },
                        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
                            Box(modifier = Modifier.padding(padding)) {


                                NavHost(
                                    navController = navController,
                                    startDestination = Home.route
                                ) {
                                    composable(Home.route) {
                                        HomeScreen(navController, scope, modalBottomSheetState)
                                    }
                                    composable(MealPrep.route) {
                                        MealPlanningScreen(navController)
                                    }
                                    composable(Groceries.route) {
//                                        HomeScreen(navController)
                                    }
                                    composable(Settings.route) {
//                                        HomeScreen(navController)
                                    }

                                    composable(
                                        DishDetails.route + "/{${DishDetails.argDishId}}",
                                        arguments = listOf(navArgument(DishDetails.argDishId) {
                                            type = NavType.IntType
                                        })
                                    ) { backStackEntry ->
                                        val id =
                                            requireNotNull(
                                                backStackEntry.arguments?.getInt(
                                                    DishDetails.argDishId
                                                )
                                            ) { "Dish id is null" }
                                        DishDetails(id, navController)
                                    }

                                    composable(
                                        MealPrepForSpecificDay.route + "/{${MealPrepForSpecificDay.argDayId}}",
                                        arguments = listOf(navArgument(MealPrepForSpecificDay.argDayId) {
                                            type = NavType.IntType
//
                                        })
                                    ){backStackEntry ->
                                        val id =
                                            requireNotNull(
                                                backStackEntry.arguments?.getInt(
                                                    MealPrepForSpecificDay.argDayId
                                                )
                                            ) { "Dish id is null" }

                                        MealPrepForSpecificDay(id, navController)
                                    }


                                }

                            }
                        },

                        backgroundColor = Color.White,

                        )


                }
            }


        }
    }
}




