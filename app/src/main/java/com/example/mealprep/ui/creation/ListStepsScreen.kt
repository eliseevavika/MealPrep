package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.mealprep.data.model.Steps
import com.example.mealprep.viewmodel.RecipeViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalUnitApi::class)
@Composable
fun ListStepsScreen(
    viewModel: () -> RecipeViewModel
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = { KeyboardHandlingDemo(viewModel) },
            content = { padding ->
                Box(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 10.dp,
                        bottom = 30.dp,
                        end = 10.dp
                    )
                ) {
                    val stepList = viewModel().listSteps.observeAsState().value

                    LazyColumn {
                        if (!stepList.isNullOrEmpty()) {
                            items(stepList) { item ->
                                Column(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillParentMaxWidth()
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
                                        setUpInstructions(viewModel, item)
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

@ExperimentalComposeUiApi
@Composable
fun KeyboardHandlingDemo(viewModel: () -> RecipeViewModel) {
    var input by remember { mutableStateOf("") }

    val callback = {
        viewModel().performQuerySteps(input)
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
                            text = "Add cooking instruction",
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp
                        )
                    })
            }
            OutlinedButton(modifier = Modifier
                .background(MealPrepColor.white)
                .padding(start = 8.dp, top = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MealPrepColor.black),
                border = BorderStroke(1.dp, MealPrepColor.black),
                shape = RoundedCornerShape(50),
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
fun setUpInstructions(
    viewModel: () -> RecipeViewModel,
    item: Steps
) {
    var input by remember { mutableStateOf(" ") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp),

        ) {

        val focusManager = LocalFocusManager.current

        val callback = {
            viewModel().setNameSteps(item, input)
            focusManager.clearFocus()
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(7f), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_brightness_1_24),
                tint = MealPrepColor.orange,
                contentDescription = "Icon",
                modifier = Modifier.size(16.dp)
            )
            TextField(
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        callback()
                        input = " "
                    }
                ),
                value = if (input == " ") item.description else input,

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
                    viewModel().removeElementSteps(item)
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