package com.example.mealprep.fill.out.recipe.card


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.pow


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel
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

@Composable
private fun IngredientsList(
    viewModel: SearchViewModel
) {
    val ingredientsList = viewModel.list.observeAsState().value
    var selectedIndex by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = Modifier.padding(bottom = 48.dp)
    ) {

        if (!ingredientsList.isNullOrEmpty()) {
            items(ingredientsList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
//                        .selectable(selected = item.id == selectedIndex, onClick = {
//                            if (selectedIndex != item.id)
//                                selectedIndex = item.id else selectedIndex = -1
//                        })
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = if (selectedIndex == ingredientsList.indexOf(item)) Icons.Filled.Check else Icons.Filled.Add,
                        contentDescription = "Select Item"
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground,
                    )
                }

            }
        }
    }
}

//@Composable
//private fun SearchAppBar(
//    viewModel: SearchViewModel,
//) {
//    // Immediately update and keep track of query from text field changes.
//    var query: String by rememberSaveable { mutableStateOf("") }
//    var showClearIcon by rememberSaveable { mutableStateOf(false) }
//
//    if (query.isEmpty()) {
//        showClearIcon = false
//    } else if (query.isNotEmpty()) {
//        showClearIcon = true
//    }
//
//    TextField(
//
//        value = query,
//        onValueChange = { onQueryChanged ->
//            // If user makes changes to text, immediately updated it.
//            query = onQueryChanged
//            // To avoid crash, only query when string isn't empty.
//            if (onQueryChanged.isNotEmpty()) {
//                // Pass latest query to refresh search results.
//                viewModel.performQuery(onQueryChanged)
//            }
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Rounded.Search,
//                tint = MaterialTheme.colors.onBackground,
//                contentDescription = "Search Icon"
//            )
//        },
//        trailingIcon = {
//            if (showClearIcon) {
//                IconButton(onClick = { query = "" }) {
//                    Icon(
//                        imageVector = Icons.Rounded.Clear,
//                        tint = MaterialTheme.colors.onBackground,
//                        contentDescription = "Clear Icon"
//                    )
//                }
//            }
//        },
//        maxLines = 1,
//        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
//        placeholder = { Text(text = "Example, 2 lb tomatoes or paste multiple ingredients with amount") },
//        textStyle = MaterialTheme.typography.subtitle1,
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = MaterialTheme.colors.background, shape = RectangleShape)
//    )
//}

@ExperimentalComposeUiApi
@Composable
fun KeyboardHandlingDemo3(viewModel: SearchViewModel) {
    val setGroceries : Set<String> = setOf("milk", "bread","potato","tomatoes","onion", "red onion", "carrot","strawberries", "cheese", "avocado")
    val setAmounts : Set<String> = setOf("lb", "kg","g","c","count", "ml", "l","oz")

    val kc = LocalSoftwareKeyboardController.current
    var input by remember { mutableStateOf("") }
    var resultNumber by remember { mutableStateOf("") }
    var resultAmount by remember { mutableStateOf("") }
    var resultIngredientName by remember { mutableStateOf("") }

    val callback = {
        resultIngredientName =  ""
        resultAmount = ""

        resultNumber = input.filter { it.isDigit() }
        val words = input.split("\\s".toRegex()).toTypedArray()
        words.forEach {
            if(setGroceries.contains(it)){
                resultIngredientName += it
            }
            else if(setAmounts.contains(it)){
                resultAmount += it
            }else if(it != resultNumber){
                resultIngredientName += it
            }
        }

        viewModel.performQuery(resultNumber + resultAmount, resultIngredientName)
    }
//    kc?.hide()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row {
            TextField(modifier = Modifier
                .padding(bottom = 16.dp),
//                .alignByBaseline(),
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
            )
            Button(modifier = Modifier
                .padding(start = 8.dp)
                .alignByBaseline(),
                onClick = {
                    callback()
                    input = ""

                }) {
                Text(text = "Add")
            }
        }

//        Row() {
//            Text(
//                text = resultNumber + resultAmount,
//                fontFamily = fontFamilyForBodyB1,
//                fontSize = 16.sp
//            )
//            Text(
//                text = resultIngredientName,
//                fontFamily = fontFamilyForBodyB2,
//                fontSize = 16.sp
//            )
//        }

    }
}



