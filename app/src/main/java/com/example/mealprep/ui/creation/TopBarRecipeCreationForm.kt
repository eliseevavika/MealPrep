package com.example.mealprep.fill.out.recipe.card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
import com.example.mealprep.ui.theme.fontFamilyForError

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarRecipeCreationForm(
    navController: () -> NavHostController,
    viewModel: () -> RecipeViewModel,
    focusRequester: FocusRequester
) {
    val chosenTabIndex by viewModel().chosenTabIndex.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
            .fillMaxWidth(60f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            navController().popBackStack("home", inclusive = false)
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
                if (viewModel().isRquiredDataEntered()) {
                    viewModel().addNewRecipe()
                    navController()?.navigate(Home.route)
                } else {
                    if (chosenTabIndex == 1 || chosenTabIndex == 2) {
                        showDialog = true
                    }
                    focusRequester.requestFocus()
                }
            }) {
            Text(
                text = "Save",
                fontFamily = fontFamilyForBodyB2,
                fontSize = 20.sp,
                color = MealPrepColor.orange
            )
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Column(Modifier.padding(start = 30.dp, top = 10.dp)) {
                        Row() {
                            Text(
                                "The ",
                                color = MealPrepColor.black,
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                            )
                            Text(
                                "Title ",
                                color = MealPrepColor.error,
                                fontFamily = fontFamilyForError,
                                fontSize = 16.sp,
                            )
                            Text(
                                "field is required.",
                                color = MealPrepColor.black,
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                            )
                        }
                        Row() {
                            Text(
                                "Give your recipe a name.",
                                color = MealPrepColor.black,
                                fontFamily = fontFamilyForBodyB2,
                                fontSize = 16.sp,
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(
                            text = "Ok", color = MealPrepColor.black,
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp,
                        )
                    }
                }, backgroundColor = MealPrepColor.white, contentColor = MealPrepColor.black
            )
        }
    }
}