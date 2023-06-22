package com.example.mealprep.fill.out.recipe.card


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealprep.Ingredient
import com.example.mealprep.NoRippleInteractionSource
import com.example.mealprep.bounceClick
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R


@OptIn(ExperimentalUnitApi::class)
@Composable
fun GroceriesAdditionScreen(
    navController: NavHostController,
    viewModel: () -> RecipeCreationViewModel
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = { TopBar(viewModel, navController) },
            content = { padding ->
                Box(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 10.dp,
                        bottom = 30.dp,
                        end = 10.dp
                    )
                ) {
                    val listExtraGroceries = viewModel().listExtraGroceries.observeAsState().value

                    Column {
                        if (!listExtraGroceries.isNullOrEmpty()) {
                            listExtraGroceries.forEach { item ->
                                key(item.id) {
                                    Column(
                                        modifier = Modifier
                                            .background(Color.White)
                                            .padding(end = 10.dp, top = 10.dp, start = 10.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .background(MaterialTheme.colors.background)
                                                .border(1.dp, MealPrepColor.grey_400)

                                        ) {
                                            setUpGroceries(viewModel, item)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBar(viewModel: () -> RecipeCreationViewModel, navController: NavHostController) {
    Column {
        TopBarGroceriesAdditionForm(viewModel, navController)
        KeyboardHandlingSearch(viewModel)
    }
}

@ExperimentalComposeUiApi
@Composable
fun KeyboardHandlingSearch(viewModel: () -> RecipeCreationViewModel) {
    var input by remember { mutableStateOf("") }

    val callback = {
        viewModel().performQueryForExtraGroceries(input)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Box(modifier = Modifier.width(280.dp)) {
                TextField(

                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            callback()
                            input = ""
                        }
                    ),
                    value = input,
                    onValueChange = {
                        input = it
                    },
                    textStyle = TextStyle(color = MealPrepColor.black),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MealPrepColor.white,
                        cursorColor = MealPrepColor.black,
                        focusedIndicatorColor = MealPrepColor.black,
                        unfocusedIndicatorColor = MealPrepColor.black,
                        focusedLabelColor = MealPrepColor.grey_800,
                        unfocusedLabelColor = MealPrepColor.grey_800
                    ),
                    placeholder = {
                        Text(
                            text = "Add extra ingredients to your list",
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp
                        )
                    })
            }
            OutlinedButton(modifier = Modifier
                .bounceClick()
                .background(MealPrepColor.white)
                .padding(start = 8.dp, top = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MealPrepColor.black),
                border = BorderStroke(1.dp, MealPrepColor.black),
                shape = RoundedCornerShape(50),
                interactionSource = NoRippleInteractionSource(),
                onClick = {
                    callback()
                    input = ""
                }) {
                Text(
                    text = "Add",
                    fontFamily = fontFamilyForBodyB2,
                    fontSize = 16.sp,
                    color = MealPrepColor.black
                )
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun setUpGroceries(
    viewModel: () -> RecipeCreationViewModel,
    item: Ingredient
) {
    var input by remember { mutableStateOf(" ") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),

        ) {
        val focusManager = LocalFocusManager.current

        val callback = {
            viewModel().setNameForExtraGrocery(item, input)
            focusManager.clearFocus()
        }

        Row(
            modifier = Modifier
                .weight(7f), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_brightness_1_24),
                tint = MealPrepColor.orange,
                contentDescription = "Icon Check",
                modifier = Modifier.size(16.dp)
            )
            TextField(keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        callback()
                        input = " "
                    }
                ),
                value = if (input == " ") item.name else input,

                onValueChange = {
                    input = it
                },
                textStyle = TextStyle(color = MealPrepColor.black),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MealPrepColor.white,
                    cursorColor = MealPrepColor.black,
                    focusedIndicatorColor = MealPrepColor.black,
                    unfocusedIndicatorColor = MealPrepColor.black,
                    focusedLabelColor = MealPrepColor.grey_800,
                    unfocusedLabelColor = MealPrepColor.grey_800
                )
            )
        }

        Row(modifier = Modifier.weight(0.5f)) {
            IconButton(
                onClick = {
                    viewModel().removeElementFromListExtraFroceries(item)
                },
                modifier = Modifier
                    .size(25.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Black
                )
            }
        }
    }
}