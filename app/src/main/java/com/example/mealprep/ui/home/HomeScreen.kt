package com.example.littlelemon

import android.app.AlertDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mealprep.*
import com.example.mealprep.ui.navigation.BottomNavigationBar
import com.example.mealprep.ui.navigation.Home
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    navController: () -> NavHostController,
    scope: () -> CoroutineScope,
    modalBottomSheetState: () -> ModalBottomSheetState,
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
            MyFloatingActionButton(
                scope, modalBottomSheetState
            )
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