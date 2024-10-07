package com.sliceuptest.mealprep.ui.editing

import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sliceuptest.mealprep.ui.modifiers.NoRippleInteractionSource
import com.sliceuptest.mealprep.ui.modifiers.bounceClick
import com.sliceuptest.mealprep.ui.navigation.Home
import com.sliceuptest.mealprep.viewmodel.RecipeViewModel
import com.sliceuptest.mealprep.ui.theme.MealPrepColor
import com.sliceuptest.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceuptest.mealprep.ui.theme.fontFamilyForBodyB2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarRecipeEditingForm(
    recipeId: Long,
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel,
    showMessageNameIsRequired: () -> Unit,
    showMessageIfGoBack: () -> Unit
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(backDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showMessageIfGoBack()
            }
        }
        backDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
            .fillMaxWidth(0.6f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            showMessageIfGoBack()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
        Text(
            text = "Editing Form", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp
        )
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.transparent),
            interactionSource = NoRippleInteractionSource(),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            shape = RoundedCornerShape(50),
            modifier = Modifier.bounceClick(),
            onClick = {
                if (viewModel().isRquiredDataEntered()) {
                    viewModel().editExistingRecipe(recipeId)
                    navController().navigate(Home.route)
                } else {
                    showMessageNameIsRequired()
                }
            }) {
            Text(
                text = "Edit",
                fontFamily = fontFamilyForBodyB2,
                fontSize = 20.sp,
                color = MealPrepColor.orange
            )
        }
    }
}