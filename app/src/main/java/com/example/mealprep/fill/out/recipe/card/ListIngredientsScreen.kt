package com.example.mealprep.fill.out.recipe.card


import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
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
                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 30.dp, end = 10.dp )) {
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
    val context = LocalContext.current
//    val swipeAbleState = rememberSwipeableState(initialValue = 0)


    LazyColumn {
        if (!ingredientsList.isNullOrEmpty()) {
            items(ingredientsList) { item ->
                SwipeAbleItemCell(viewModel, item)
            }
        }

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

@OptIn(ExperimentalUnitApi::class)
@ExperimentalMaterialApi
@Composable
fun SwipeAbleItemCell(
    viewModel: ListIngredientsViewModel,
    item: Groceries
) {
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
                        .height(50.dp)
                        .background(MaterialTheme.colors.background)
                        .border(1.dp, MealPrepColor.grey_400)

                ) {

                    setUpRow(viewModel, item)
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
//        horizontalArrangement = Arrangement.Start,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp),

        ) {
        IngredientView(item)

        EditDelete(viewModel, item)
    }
}
@Composable
fun IngredientView(item: Groceries) {
    Row(modifier = Modifier.fillMaxHeight(),verticalAlignment = Alignment.CenterVertically,) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Select Item",
            Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(width = 8.dp))

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

@Composable
fun EditDelete(viewModel: ListIngredientsViewModel, item: Groceries) {
    Row() {
        IconButton(
            onClick = {

            },
            modifier = Modifier
                .size(25.dp)
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(width = 16.dp))

        IconButton(
            onClick = {
                viewModel.removeElement(item)
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




