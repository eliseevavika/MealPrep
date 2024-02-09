package com.example.mealprep.ui.creation

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
import com.example.mealprep.*
import com.example.mealprep.ui.modifiers.NoRippleInteractionSource
import com.example.mealprep.ui.modifiers.bounceClick
import com.example.mealprep.ui.navigation.Home
import com.example.mealprep.viewmodel.RecipeViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarRecipeCreationForm(
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel,
    showMessageNameIsRequired: () -> Unit,
    showMessageUrlIsIncorrect: () -> Unit,
    showMessageNameIsRequiredAndUrlISIncorrect: () -> Unit,
    showMessageIfGoBack: () -> Unit
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val isValidUrl = viewModel().isValidUrl.collectAsState()

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
            .fillMaxWidth(60f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            showMessageIfGoBack()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
        Text(
            text = "Recipe Form", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp
        )
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.transparent),
            interactionSource = NoRippleInteractionSource(),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            shape = RoundedCornerShape(50),
            modifier = Modifier.bounceClick(),
            onClick = {
                when {
                    viewModel().isRquiredDataEntered() && isValidUrl.value -> {
                        viewModel().addNewRecipe()
                        navController().navigate(Home.route)
                    }
                    !viewModel().isRquiredDataEntered() && isValidUrl.value -> showMessageNameIsRequired()
                    viewModel().isRquiredDataEntered() && !isValidUrl.value -> showMessageUrlIsIncorrect()
                    else -> showMessageNameIsRequiredAndUrlISIncorrect()
                }
            }) {
            Text(
                text = "Save",
                fontFamily = fontFamilyForBodyB2,
                fontSize = 20.sp,
                color = MealPrepColor.orange
            )
        }
    }
}