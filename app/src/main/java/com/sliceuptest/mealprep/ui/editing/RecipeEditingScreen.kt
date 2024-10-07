package com.sliceuptest.mealprep.ui.editing

import android.app.AlertDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.sliceuptest.mealprep.ui.creation.TabScreen
import com.sliceuptest.mealprep.viewmodel.RecipeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecipeEditingScreen(
    recipeId: Long,
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
                TopBarRecipeEditingForm(
                    recipeId,
                    navController,
                    viewModal,
                    showMessageNameIsRequired = {
                        AlertDialog.Builder(context)
                            .setMessage("The title field is required. Give your recipe a name.")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    },
                    showMessageIfGoBack = {
                        if (viewModal().checkIfSomethingFilled()) {
                            AlertDialog.Builder(context)
                                .setMessage("Are you sure you want to go back? Any filled data will be lost.")
                                .setPositiveButton("Stay") { dialog, _ ->
                                    dialog.dismiss()
                                }.setNegativeButton("Go back") { dialog, _ ->
                                    viewModal().emptyLiveData()
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