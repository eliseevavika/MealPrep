package com.sliceup.mealprep.ui.home

import android.app.AlertDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.sliceup.mealprep.data.Recipe
import com.sliceup.mealprep.ui.navigation.BottomNavigationBar
import com.sliceup.mealprep.ui.navigation.Home
import com.sliceup.mealprep.ui.navigation.RecipeCreation
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.viewmodel.RecipeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel
) {
    val context = LocalContext.current
    val currentUser = auth.currentUser
    if (currentUser == null) {
        viewModel().signAsAGuest(
            onSuccess = { uid ->
                navController().navigate(Home.route)
            },
            onError = { error ->
                AlertDialog.Builder(context)
                    .setMessage(error)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        )
    }

    val recipeList: List<Recipe> by viewModel().allRecipes.observeAsState(initial = listOf())

    Scaffold(topBar = {
        TopAppBarHome()
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController().navigate(RecipeCreation.route)
                },
                modifier = Modifier.padding(all = 16.dp),
                backgroundColor = MealPrepColor.orange,
                contentColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = MealPrepColor.white
                )
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column {
                    RecipesFeed(
                        navController, { recipeList }, false, viewModel, -1
                    )
                }
            }
        })
}