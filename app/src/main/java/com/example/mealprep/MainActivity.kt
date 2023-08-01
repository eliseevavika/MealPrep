package com.example.mealprep

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
import androidx.compose.runtime.collectAsState
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
import com.example.littlelemon.DishDetails
import com.example.littlelemon.HomeScreen
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.GroceriesAdditionScreen
import com.example.mealprep.fill.out.recipe.card.GroceriesScreen
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationScreen
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPlanningScreen
import com.example.mealprep.fill.out.recipe.card.mealplanning.MealPrepForSpecificDay
import com.example.mealprep.fill.out.recipe.card.settings.AccountScreen
import com.example.mealprep.ui.authentication.*
import com.example.mealprep.ui.navigation.*
import com.example.mealprep.ui.theme.MealPrepTheme
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.reflect.Field


class MainActivity : ComponentActivity() {

    lateinit var viewModel: RecipeViewModel
    lateinit var authViewModel: LoginScreenViewModel
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


                authViewModel = ViewModelProvider(
                    this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                ).get(LoginScreenViewModel::class.java)

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
                    Scaffold(
                        content = { padding ->
                            Box(modifier = Modifier.padding(padding)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = getStartDestination(
                                        viewModel, authViewModel
                                    )
                                ) {
                                    composable(LoginScreen.route) {
                                        LoginScreen(authViewModel, navController)
                                    }

                                    composable(ForgotPasswordScreen.route) {
                                        ForgotPasswordScreen(authViewModel) {
                                            navController.popBackStack()
                                        }
                                    }

                                    composable(SignUpScreen.route) {
                                        SignUpScreen(
                                            authViewModel, auth
                                        ) { navController.popBackStack() }
                                    }
                                    composable(VerifyEmailScreen.route) {
                                        VerifyEmailScreen(
                                            authViewModel, auth
                                        ) { navController.navigate(Home.route) }
                                    }

                                    composable(Home.route) {
                                        HomeScreen({ navController },
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
                                        DishDetails.route + "/{${DishDetails.argDishId}}",
                                        arguments = listOf(navArgument(DishDetails.argDishId) {
                                            type = NavType.LongType
                                        })
                                    ) { backStackEntry ->
                                        val id = requireNotNull(
                                            backStackEntry.arguments?.getLong(
                                                DishDetails.argDishId
                                            )
                                        ) { "Dish id is null" }
                                        DishDetails(id, { navController }) { viewModel }
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

                                        MealPrepForSpecificDay(dayId,
                                            { navController }) { viewModel }
                                    }

                                    composable(GroceriesAddition.route) {
                                        GroceriesAdditionScreen({ navController }) { viewModel }
                                    }

                                    composable(RecipeCreation.route) {
                                        RecipeCreationScreen(
                                            { navController },
                                        ) { viewModel }
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

@Composable
private fun getStartDestination(
    viewModel: RecipeViewModel, authViewModel: LoginScreenViewModel
): String {
    val isUserSignedOut = authViewModel.getAuthState().collectAsState().value
    val isUserVerified = authViewModel.isEmailVerified
    val isUserAnonymous = authViewModel.isUserAnonymous

    if (isUserSignedOut) {
        return LoginScreen.route
    } else {
        if (isUserAnonymous || isUserVerified) {
            viewModel.refreshDataHomeForCurrentUser()
            return Home.route
        } else {
            return VerifyEmailScreen.route
        }
    }
}