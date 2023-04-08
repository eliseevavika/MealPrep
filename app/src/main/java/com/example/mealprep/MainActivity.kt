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
import com.example.mealprep.fill.out.recipe.card.GroceriesAdditionScreen
import com.example.mealprep.fill.out.recipe.card.GroceriesScreen
import com.example.mealprep.fill.out.recipe.card.groceries.GroceriesViewModel
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningScreen
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningViewModel
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPrepForSpecificDay
import com.example.mealprep.fill.out.recipe.card.settings.SettingsScreen
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
                        content = { padding ->
                            Box(modifier = Modifier.padding(padding)) {
                                val viewModel = MealPlanningViewModel()
                                var chosenDay: Int? = null

                                NavHost(
                                    navController = navController, startDestination = Home.route
                                ) {
                                    composable(Home.route) {
                                        HomeScreen(
                                            navController, scope, modalBottomSheetState, viewModel
                                        )
                                    }

                                    composable(MealPrep.route) {
                                        MealPlanningScreen(navController, viewModel, chosenDay)
                                    }

                                    composable(Groceries.route) {
                                        GroceriesScreen(navController, GroceriesViewModel())

                                    }

                                    composable(Settings.route) {
                                        SettingsScreen(navController, GroceriesViewModel())
                                    }

                                    composable(
                                        DishDetails.route + "/{${DishDetails.argDishId}}",
                                        arguments = listOf(navArgument(DishDetails.argDishId) {
                                            type = NavType.IntType
                                        })
                                    ) { backStackEntry ->
                                        val id = requireNotNull(
                                            backStackEntry.arguments?.getInt(
                                                DishDetails.argDishId
                                            )
                                        ) { "Dish id is null" }
                                        DishDetails(id, navController)
                                    }

                                    composable(
                                        MealPrepForSpecificDay.route + "/{${MealPrepForSpecificDay.argDayId}}",
                                        arguments = listOf(navArgument(
                                            MealPrepForSpecificDay.argDayId
                                        ) {
                                            type = NavType.IntType
                                        })
                                    ) { backStackEntry ->
                                        val dayId = requireNotNull(
                                            backStackEntry.arguments?.getInt(
                                                MealPrepForSpecificDay.argDayId
                                            )
                                        ) { "Dish id is null" }
                                        chosenDay = dayId

                                        MealPrepForSpecificDay(dayId, navController, viewModel)
                                    }

                                    composable(GroceriesAddition.route) {
                                        GroceriesAdditionScreen(navController, GroceriesViewModel())
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