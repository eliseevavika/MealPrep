package com.example.mealprep.fill.out.recipe.card.creation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.mealprep.fill.out.recipe.card.TabScreen
import com.example.mealprep.fill.out.recipe.card.TopBarRecipeCreationForm
import kotlinx.coroutines.CoroutineScope


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeCreationScreen(
    navController: () -> NavHostController,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    viewModal: () -> RecipeCreationViewModel,
) {
    val focusRequester = remember { FocusRequester() }

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = { TopBarRecipeCreationForm(navController, viewModal, focusRequester) },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    TabScreen(viewModal, focusRequester)
                }
            },
            backgroundColor = Color.White,
        )
    }
}