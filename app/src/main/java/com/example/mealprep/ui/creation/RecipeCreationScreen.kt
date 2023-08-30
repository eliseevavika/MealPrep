package com.example.mealprep.fill.out.recipe.card.creation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mealprep.fill.out.recipe.card.TabScreen
import com.example.mealprep.fill.out.recipe.card.TopBarRecipeCreationForm
import com.example.mealprep.viewmodel.RecipeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecipeCreationScreen(
    navController: () -> NavHostController,
    viewModal: () -> RecipeViewModel,
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = {
                TopBarRecipeCreationForm(navController, viewModal, showMessageNameIsRequired = {
                    android.app.AlertDialog.Builder(context)
                        .setMessage("The title field is required. Give your recipe a name.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }, showMessageIfGoBack = {
                    if (viewModal().checkIfSomethingFilled()) {
                        android.app.AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to go back? Any filled data will be lost.")
                            .setPositiveButton("Stay") { dialog, _ ->
                                dialog.dismiss()
                            }.setNegativeButton("Go back") { dialog, _ ->
                                navController().popBackStack("home", inclusive = false)
                            }.show()
                    } else {
                        navController().popBackStack("home", inclusive = false)
                    }
                })
            },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    TabScreen(viewModal, focusRequester)
                }
            },
            backgroundColor = Color.White,
        )
    }
}