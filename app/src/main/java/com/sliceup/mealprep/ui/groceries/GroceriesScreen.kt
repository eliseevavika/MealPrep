package com.sliceup.mealprep.ui.groceries

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sliceup.mealprep.data.Ingredient
import com.sliceup.mealprep.ui.navigation.BottomNavigationBar
import com.sliceup.mealprep.ui.navigation.GroceriesAddition
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalUnitApi::class)
@Composable
fun GroceriesScreen(
    navController: () -> NavHostController, viewModel: () -> RecipeViewModel
) {
    val scrollState = rememberScrollState()
    val expandMainStore = viewModel().expandMainStore.collectAsState().value
    var expandCompleted by remember { mutableStateOf(false) }

    val listGroceries =
        viewModel().ingredientsFromMealPlans.observeAsState(listOf()).value.sortedBy { it.first.aisle }
            .groupBy { it.first.aisle }
            .flatMap { (_, groupedList) -> groupedList.sortedBy { it.first.short_name } }

    val completedIngredients =
        viewModel().completedIngredients.observeAsState(listOf()).value.sortedByDescending { it.completion_date }

    Scaffold(topBar = {
        TopBarForGroceriesScreen(viewModel)
    },
        bottomBar = { BottomNavigationBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController().navigate(GroceriesAddition.route)
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
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = 16.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {
                Column(
                    Modifier.wrapContentHeight()
                ) {
                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
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
                                }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    tint = MealPrepColor.orange,
                                    contentDescription = "Drop Down Arrow"
                                )
                            }
                        }
                        if (expandMainStore) {
                            if (listGroceries.isNotEmpty()) {
                                listGroceries.forEach { item ->
                                    Column(
                                        modifier = Modifier.background(Color.White),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        setUpLines(
                                            item.first, item.second, viewModel, false, completedIngredients
                                        )
                                    }
                                }
                            }
                        }
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
                                }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    tint = MealPrepColor.orange,
                                    contentDescription = "Drop Down Arrow"
                                )
                            }
                        }
                        if (expandCompleted) {
                            if (completedIngredients.isNotEmpty()) {
                                completedIngredients.forEach { item ->
                                    key(item.id) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            setUpLines(
                                                item, 0, viewModel, true, completedIngredients
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@ExperimentalUnitApi
@Composable
fun setUpLines(
    item: Ingredient,
    itemCount: Int,
    viewModel: () -> RecipeViewModel,
    isCompleted: Boolean,
    completedIngredients: List<Ingredient>?
) {
    val context = LocalContext.current
    val tooltipState = remember { RichTooltipState() }
    val scope = rememberCoroutineScope()
    val recipeNameForTooltip = viewModel().recipeNameForTooltip.observeAsState().value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(start = 10.dp, top = 10.dp, end = 8.dp, bottom = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = completedIngredients?.contains(item) ?: false,
                modifier = Modifier.size(16.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MealPrepColor.orange, unselectedColor = MealPrepColor.black
                ),
                onClick = {
                    viewModel().performQueryForGroceries(item)
                })
            Spacer(modifier = Modifier.width(width = 8.dp))

            Text(
                modifier = Modifier
                    .weight(9f)
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
                    .weight(3f).padding(start = 10.dp)
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
                        Icon(imageVector = Icons.Rounded.Restaurant, contentDescription = "")
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

            Spacer(modifier = Modifier.weight(1f))

            IngredientSettingOptions(item,
                isCompleted,
                viewModel = viewModel(),
                modifier = Modifier.weight(1f),
                showMessage = { ingredient, message ->
                    val name = ingredient.name.substringBeforeLast(",")
                    val spannableString = SpannableString(message)
                    val nameStart = message.indexOf(name)
                    val nameEnd = nameStart + name.length
                    spannableString.setSpan(
                        ForegroundColorSpan(MealPrepColor.orange.toArgb()),
                        nameStart,
                        nameEnd,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                    android.app.AlertDialog.Builder(context).setMessage(spannableString)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                },
                showMessageForAisleUpdate = { ingredient, aisle ->
                    val ingredientName = ingredient.name.substringBeforeLast(",")
                    val message =
                        "Ingredient ${ingredientName} moved to ${aisle.departmentName} aisle"

                    val spannableString = SpannableString(message)
                    val nameStart = message.indexOf(ingredientName)
                    val nameEnd = nameStart + ingredientName.length

                    if (nameStart != -1) {
                        spannableString.setSpan(
                            ForegroundColorSpan(MealPrepColor.orange.toArgb()), // Change color here
                            nameStart, nameEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                        )
                    }

                    val aisleStart = message.indexOf(aisle.departmentName!!)
                    if (aisleStart != -1) {
                        spannableString.setSpan(
                            ForegroundColorSpan(MealPrepColor.orange.toArgb()),
                            aisleStart,
                            aisleStart + aisle.departmentName.length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    android.app.AlertDialog.Builder(context).setMessage(spannableString)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                })
        }
    }
}