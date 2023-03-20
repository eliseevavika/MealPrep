package com.example.mealprep.fill.out.recipe.card


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import java.util.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListIngredientsScreen(
    viewModel: ListIngredientsViewModel
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = { KeyboardHandlingDemo3(viewModel) },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    IngredientsList(viewModel)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
@Composable
private fun IngredientsList(
    viewModel: ListIngredientsViewModel
) {
    val ingredientsList = viewModel.list.observeAsState().value

    LazyColumn{

        if (!ingredientsList.isNullOrEmpty()) {
            items(ingredientsList) { item ->
                val dismissState = rememberDismissState(initialValue = DismissValue.Default)
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.removeElement(item)
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(
                        DismissDirection.EndToStart
                    ),
                    dismissThresholds = { direction ->
                        FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                    },
                    background = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.Default -> Color.White
                                else -> Color.Red
                            }
                        )
                        val alignment = Alignment.CenterEnd
                        val icon = Icons.Default.Delete

                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                        )

                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = Dp(20f)),
                            contentAlignment = alignment
                        ) {
                            Icon(
                                icon,
                                contentDescription = "Delete Icon",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    },
                    dismissContent = {

                        Card(
                            elevation = animateDpAsState(
                                if (dismissState.dismissDirection != null) 4.dp else 0.dp
                            ).value,
                        ) {
                            setUpRow(viewModel, item)
                        }
                    }
                )


            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun setUpRow(
    viewModel: ListIngredientsViewModel,
    item: Groceries

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = {
            viewModel.removeElement(item)
        }) {
            Icon(

                imageVector = Icons.Filled.Check,
                contentDescription = "Select Item"
            )
        }

//        Spacer(modifier = Modifier.width(width = 8.dp))
//                    Text(
//                        text = item.name,
//                        style = MaterialTheme.typography.h6,
//                        color = MaterialTheme.colors.onBackground,
//                    )

        Text(
            text = item.amount,
            fontFamily = fontFamilyForBodyB2,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(
            text = item.name,
            fontFamily = fontFamilyForBodyB2,
            fontSize = 16.sp
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun KeyboardHandlingDemo3(viewModel: ListIngredientsViewModel) {
    val setGroceries: Set<String> = setOf(
        "milk",
        "bread",
        "potato",
        "tomatoes",
        "onion",
        "red onion",
        "carrot",
        "strawberries",
        "cheese",
        "avocado"
    )
    val setAmounts: Set<String> = setOf("lb", "kg", "g", "c", "count", "ml", "l", "oz")

    val kc = LocalSoftwareKeyboardController.current
    var input by remember { mutableStateOf("") }
    var resultNumber by remember { mutableStateOf("") }
    var resultAmount by remember { mutableStateOf("") }
    var resultIngredientName by remember { mutableStateOf("") }

    val callback = {
        resultIngredientName = ""
        resultAmount = ""

        resultNumber = input.filter { it.isDigit() }
        val words = input.split("\\s".toRegex()).toTypedArray()
        words.forEach {
            if (setGroceries.contains(it)) {
                resultIngredientName += it
            } else if (setAmounts.contains(it)) {
                resultAmount += it
            } else if (it != resultNumber) {
                resultIngredientName += it
            }
        }

        viewModel.performQuery(resultNumber + resultAmount, resultIngredientName)
    }
//    kc?.hide()

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
                        text = "Add one item or paste multiple",
                        fontFamily = fontFamilyForBodyB2,
                        fontSize = 16.sp
                    )
                })

            OutlinedButton(modifier = Modifier
                .background(MealPrepColor.white)
                .padding(start = 8.dp, top = 8.dp),
//                .alignByBaseline(),
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



