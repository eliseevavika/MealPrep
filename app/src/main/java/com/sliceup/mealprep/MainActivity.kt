package com.sliceup.mealprep

import android.database.CursorWindow
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sliceup.mealprep.ui.home.BottomSheetContent
import com.sliceup.mealprep.ui.home.HomeScreen
import com.sliceup.mealprep.ui.groceries.GroceriesScreen
import com.sliceup.mealprep.ui.creation.RecipeCreationScreen
import com.sliceup.mealprep.ui.mealplanning.MealPlanningScreen
import com.sliceup.mealprep.ui.mealplanning.MealPrepForSpecificDay
import com.sliceup.mealprep.ui.account.AccountScreen
import com.sliceup.mealprep.ui.editing.RecipeEditingScreen
import com.sliceup.mealprep.ui.theme.MealPrepTheme
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sliceup.mealprep.ui.details.DishDetails
import com.sliceup.mealprep.ui.navigation.Account
import com.sliceup.mealprep.ui.navigation.Groceries
import com.sliceup.mealprep.ui.navigation.GroceriesAddition
import com.sliceup.mealprep.ui.navigation.Home
import com.sliceup.mealprep.ui.navigation.MealPrep
import com.sliceup.mealprep.ui.navigation.RecipeCreation
import com.sliceup.mealprep.ui.navigation.RecipeEditing
import kotlinx.coroutines.launch
import java.lang.reflect.Field


class MainActivity : ComponentActivity() {

    lateinit var viewModel: RecipeViewModel
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            MealPrepTheme {
                try {
                    val field: Field =
                        CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
                    field.setAccessible(true)
                    field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                viewModel = ViewModelProvider(
                    this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                ).get(RecipeViewModel::class.java)

                val navController = rememberNavController()

                val modalBottomSheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                val scope = rememberCoroutineScope()

                BackHandler(modalBottomSheetState.isVisible) {
                    scope.launch { modalBottomSheetState.hide() }
                }
                ModalBottomSheetLayout(
                    sheetContent = {
                        BottomSheetContent({ navController }, scope, modalBottomSheetState)
                    },
                    Modifier.fillMaxWidth(),
                    sheetState = modalBottomSheetState,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    sheetBackgroundColor = Color.Blue,
                ) {
                    Scaffold(content = { padding ->
                        Box(modifier = Modifier.padding(0.dp)) {
                            NavHost(
                                navController = navController,
                                startDestination = getStartDestination(viewModel)
                            ) {
                                composable(Home.route) {
                                    HomeScreen(auth,
                                        { navController },
                                        { scope },
                                        { modalBottomSheetState }) { viewModel }
                                }

                                composable(MealPrep.route) {
                                    viewModel.refreshDataMealPrepForCurrentUser()
                                    MealPlanningScreen({ navController }) { viewModel }
                                }

                                composable(Groceries.route) {
                                    viewModel.refreshDataGroceriesForCurrentUser()
                                    GroceriesScreen({ navController }) { viewModel }
                                }

                                composable(Account.route) {
                                    AccountScreen({ navController }) { viewModel }
                                }

                                composable(
                                    com.sliceup.mealprep.ui.navigation.DishDetails.route + "/{${com.sliceup.mealprep.ui.navigation.DishDetails.argDishId}}" + "/{${com.sliceup.mealprep.ui.navigation.DishDetails.mealPrepOn}}",
                                    arguments = listOf(navArgument(com.sliceup.mealprep.ui.navigation.DishDetails.argDishId) {
                                        type = NavType.LongType
                                    }, navArgument(com.sliceup.mealprep.ui.navigation.DishDetails.mealPrepOn) {
                                        type = NavType.BoolType
                                    })
                                ) { backStackEntry ->
                                    val id = requireNotNull(
                                        backStackEntry.arguments?.getLong(
                                            com.sliceup.mealprep.ui.navigation.DishDetails.argDishId
                                        )
                                    )
                                    val isMealPlan = requireNotNull(
                                        backStackEntry.arguments?.getBoolean(
                                            com.sliceup.mealprep.ui.navigation.DishDetails.mealPrepOn
                                        )
                                    )

                                    DishDetails(id, { navController }, { viewModel }, isMealPlan
                                    )
                                }

                                composable(
                                    com.sliceup.mealprep.ui.navigation.MealPrepForSpecificDay.route + "/{${com.sliceup.mealprep.ui.navigation.MealPrepForSpecificDay.argDayId}}",
                                    arguments = listOf(navArgument(
                                        com.sliceup.mealprep.ui.navigation.MealPrepForSpecificDay.argDayId
                                    ) {
                                        type = NavType.IntType
                                    })
                                ) { backStackEntry ->
                                    val dayId = requireNotNull(
                                        backStackEntry.arguments?.getInt(
                                            com.sliceup.mealprep.ui.navigation.MealPrepForSpecificDay.argDayId
                                        )
                                    ) { "Dish id is null" }

                                    MealPrepForSpecificDay(
                                        dayId,
                                        { navController }) { viewModel }
                                }

                                composable(RecipeCreation.route) {
                                    RecipeCreationScreen(
                                        { navController },
                                    ) { viewModel }
                                }

                                composable(
                                    RecipeEditing.route + "/{${RecipeEditing.argRecipeId}}",
                                    arguments = listOf(navArgument(RecipeEditing.argRecipeId) {
                                        type = NavType.LongType
                                    })
                                ) { backStackEntry ->
                                    val id = requireNotNull(
                                        backStackEntry.arguments?.getLong(
                                            RecipeEditing.argRecipeId
                                        )
                                    )

                                    RecipeEditingScreen(id, { navController }, { viewModel })
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
private fun getStartDestination(
    viewModel: RecipeViewModel
): String {
    viewModel.refreshDataHomeForCurrentUser()
    return Home.route
}