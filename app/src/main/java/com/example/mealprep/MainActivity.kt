package com.example.mealprep

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.littlelemon.DishDetails
import com.example.littlelemon.HomeScreen
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.MealPrepTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealPrepTheme {
                val navController = rememberNavController()

                val modalBottomSheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                val scope = rememberCoroutineScope()

                ModalBottomSheetLayout(
                    sheetContent = {
                        BottomSheetContent()
                    },
                    Modifier.fillMaxWidth(),
                    sheetState = modalBottomSheetState,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    sheetBackgroundColor = Color.Blue,
                ) {
                    Scaffold(
                        topBar = { TopAppBar() },
                        bottomBar = { BottomNavigationBar(navController = navController) },
                        floatingActionButton = { MyFloatingActionButton(scope, modalBottomSheetState)},
                        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
                            Box(modifier = Modifier.padding(padding)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Home.route
                                ) {
                                    composable(Home.route) {
                                        HomeScreen(navController)
                                    }
                                    composable(MealPrep.route) {
//                                        HomeScreen(navController)
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
                                    ) {
                                        val id =
                                            requireNotNull(it.arguments?.getInt(DishDetails.argDishId)) { "Dish id is null" }
                                        DishDetails(id)
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




