package com.sliceup.mealprep.ui.groceries


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sliceup.mealprep.data.Ingredient
import com.sliceup.mealprep.ui.navigation.BottomNavigationBar
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalUnitApi::class, ExperimentalComposeUiApi::class)
@Composable
fun GroceriesScreen(
    navController: () -> NavHostController, viewModel: () -> RecipeViewModel
) {
    val expandMainStore = viewModel().expandMainStore.collectAsState().value
    var expandCompleted by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val listGroceries =
        viewModel().ingredientsFromMealPlans.observeAsState(listOf()).value.sortedBy { it.first.aisle }
            .groupBy { it.first.aisle }
            .flatMap { (_, groupedList) -> groupedList.sortedBy { it.first.short_name } }

    var showSearch by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val completedIngredients =
        viewModel().completedIngredients.observeAsState(listOf()).value.sortedByDescending { it.completion_date }

    val returnedIngredientId by viewModel().returnedIngredientId.collectAsState()
    var highlightedItemId by remember { mutableStateOf<Long?>(-1) }
    var highlightedItemIndex by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopBarForGroceriesScreen(viewModel)
    },
        bottomBar = { BottomNavigationBar(navController = navController) },
        floatingActionButton = {
            if (!showSearch) {
                FloatingActionButton(
                    onClick = {
                        showSearch = true
                        expandCompleted = false
                    },
                    modifier = Modifier.padding(all = 16.dp),
                    backgroundColor = MealPrepColor.orange,
                    contentColor = Color.White,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = MealPrepColor.white
                    )
                }
            }
        },
        content = { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(
                    top = 16.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp, start = 8.dp, bottom = 8.dp
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Shopping list",
                            color = MealPrepColor.orange,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        IconButton(modifier = Modifier.rotate(if (expandMainStore) 180F else 0F),
                            onClick = {
                                viewModel().setExpandMainStore(!expandMainStore)
                                if(!expandMainStore){
                                    showSearch = false
                                }
                            }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                tint = MealPrepColor.orange,
                                contentDescription = "Drop Down Arrow"
                            )
                        }
                    }
                }
                if (expandMainStore) {
                    items(listGroceries) { item ->
                        val isHighlighted = item.first.id == highlightedItemId
                        Column(
                            modifier = Modifier.background(Color.White),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            setUpLines(
                                item.first,
                                item.second,
                                viewModel,
                                false,
                                completedIngredients,
                                isHighlighted
                            )
                        }
                    }
                }
                if (showSearch) {
                    expandCompleted = false
                    item {
                        LaunchedEffect(Unit) {
                            listState.scrollToItem(listState.layoutInfo.totalItemsCount - 1)
                            focusRequester.requestFocus()
                        }
                        KeyboardHandlingSearch(viewModel, focusRequester, onDone = {
                            showSearch = false
                            viewModel().setExpandMainStore(true)
                            viewModel().addExtraGroceriesToTheDB()
                        })
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp, start = 8.dp, bottom = 8.dp
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Completed",
                            color = MealPrepColor.orange,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        IconButton(modifier = Modifier.rotate(if (expandCompleted) 180F else 0F),
                            onClick = {
                                expandCompleted = !expandCompleted
                                showSearch = false
                            }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                tint = MealPrepColor.orange,
                                contentDescription = "Drop Down Arrow"
                            )
                        }
                    }
                }
                if (expandCompleted) {
                    showSearch = false

                    items(completedIngredients) { item ->
                        key(item.id) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                setUpLines(
                                    item,
                                    0,
                                    viewModel,
                                    true,
                                    completedIngredients,
                                    false
                                )
                            }
                        }
                    }
                }
            }
        })

    LaunchedEffect(showSearch, listGroceries, returnedIngredientId, highlightedItemId, listState) {
        coroutineScope.launch {
            if (showSearch) {
                expandCompleted = false
                listState.scrollToItem(listState.layoutInfo.totalItemsCount - 1)
                focusRequester.requestFocus()
            }

            if (listGroceries.isNotEmpty()) {
                returnedIngredientId?.let { id ->
                    val index =
                        listGroceries.indexOfFirst { it.first.id == returnedIngredientId }
                    if (index != -1) {
                        highlightedItemIndex = index
                        highlightedItemId = id

                    }
                }
            }
        }
    }

    LaunchedEffect(showSearch, highlightedItemId, highlightedItemIndex, listState) {
        highlightedItemIndex?.let { index ->
            listState.animateScrollToItem(index)
        }
        delay(1000)
        highlightedItemId = null
        viewModel().resetReturnedIngredientId()
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@ExperimentalUnitApi
@Composable
fun setUpLines(
    item: Ingredient,
    itemCount: Int,
    viewModel: () -> RecipeViewModel,
    isCompleted: Boolean,
    completedIngredients: List<Ingredient>?,
    isHighlighted: Boolean
) {
    val tooltipState = remember { RichTooltipState() }
    val scope = rememberCoroutineScope()
    val recipeNameForTooltip = viewModel().recipeNameForTooltip.observeAsState().value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .border(
                border = BorderStroke(
                    2.dp,
                    if (isHighlighted) MealPrepColor.orange else Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(start = 10.dp, top = 10.dp, end = 8.dp, bottom = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = completedIngredients?.contains(item) ?: false,
                modifier = Modifier.size(16.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MealPrepColor.orange,
                    unselectedColor = MealPrepColor.black
                ),
                onClick = {
                    viewModel().performQueryForGroceries(item)
                })
            Spacer(modifier = Modifier.width(width = 16.dp))

            Text(
                modifier = Modifier
                    .weight(9f)
                    .padding(end = 16.dp)
                    .combinedClickable(onClick = {}, onLongClick = {
                        scope.launch {
                            viewModel().getTextForTooltipBox(item.recipe_id)
                            tooltipState.show()
                        }
                    }),
                text = item.name,
                fontFamily = fontFamilyForBodyB2,
                style = if (isCompleted) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(
                    textDecoration = TextDecoration.None
                ),
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 16.dp)
                    .combinedClickable(onClick = {}, onLongClick = {
                        scope.launch {
                            viewModel().getTextForTooltipBox(item.recipe_id)
                            tooltipState.show()
                        }
                    }),
                text = itemCount.takeIf { it > 0 }?.toString() ?: "",
                fontFamily = fontFamilyForBodyB2,
                style = if (isCompleted) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(
                    textDecoration = TextDecoration.None
                ),
                fontSize = 16.sp
            )
            RichTooltipBox(modifier = Modifier.weight(9f), title = {
                Text(
                    item.name, color = MealPrepColor.black,
                    fontFamily = fontFamilyForBodyB2,
                    fontSize = 16.sp,
                )
            }, action = {
                TextButton(onClick = { scope.launch { tooltipState.dismiss() } }) {
                    Text(
                        "Ok", color = MealPrepColor.orange,
                        fontFamily = fontFamilyForBodyB2,
                        fontSize = 16.sp,
                    )
                }
            }, text = {
                if (recipeNameForTooltip != null) {
                    Row() {
                        Text(text = "")
                    }
                    Row() {
                        Icon(
                            imageVector = Icons.Rounded.Restaurant,
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(width = 8.dp))
                        Text(
                            recipeNameForTooltip,
                            color = MealPrepColor.black,
                            fontFamily = fontFamilyForBodyB2,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    Text(text = "")
                }
            }, tooltipState = tooltipState, content = {})
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun KeyboardHandlingSearch(
    viewModel: () -> RecipeViewModel,
    focusRequester: FocusRequester,
    onDone: () -> Unit
) {
    var input by remember { mutableStateOf("") }

    val callback = {
        viewModel().performQueryForExtraGroceries(input)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(start = 10.dp, top = 10.dp, end = 8.dp, bottom = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(modifier = Modifier
                .weight(9f)
                .padding(end = 16.dp)
                .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    callback()
                    input = ""
                    onDone()
                }),
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
    }
}