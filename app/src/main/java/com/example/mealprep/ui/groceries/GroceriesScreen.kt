package com.example.mealprep.fill.out.recipe.card

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealprep.Ingredient
import com.example.mealprep.data.model.Aisle
import com.example.mealprep.ui.groceries.IngredientSettingOptions
import com.example.mealprep.ui.navigation.BottomNavigationBar
import com.example.mealprep.ui.navigation.GroceriesAddition
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.mealprep.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalUnitApi::class)
@Composable
fun GroceriesScreen(
    navController: () -> NavHostController, viewModel: () -> RecipeViewModel
) {
    Scaffold(topBar = {
        TopBarForGroceriesScreen()
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
                    top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {
                val listGroceries =
                    viewModel().ingredientsFromMealPlans.observeAsState(listOf()).value.sortedBy { it.aisle }
                        .groupBy { it.aisle }
                        .flatMap { (_, groupedList) -> groupedList.sortedBy { it.short_name } }

                Column(
                    Modifier.wrapContentHeight()
                ) {
                    var expand by remember { mutableStateOf(false) }

                    val listGroceriesForAnotherStore =
                        viewModel().listGroceriesForAnotherStore.observeAsState(listOf()).value.sortedBy { it.aisle }
                            .groupBy { it.aisle }
                            .flatMap { (_, groupedList) -> groupedList.sortedBy { it.short_name } }

                    val completedIngredients =
                        viewModel().completedIngredients.observeAsState(listOf()).value.sortedByDescending { it.completion_date }

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (listGroceries.isNotEmpty()) {
                            listGroceries.forEach { item ->
                                Column(
                                    modifier = Modifier.background(Color.White),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    setUpLines(
                                        item, viewModel, false, completedIngredients
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp
                                ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Another store",
                                color = MealPrepColor.orange,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        if (listGroceriesForAnotherStore.isNotEmpty()) {
                            listGroceriesForAnotherStore.forEach { item ->
                                Column(
                                    modifier = Modifier.background(Color.White),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    setUpLines(
                                        item, viewModel, false, completedIngredients
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp
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
                            IconButton(
                                modifier = Modifier.rotate(if (expand) 180F else 0F),
                                onClick = {
                                    expand = !expand
                                }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    tint = MealPrepColor.orange,
                                    contentDescription = "Drop Down Arrow"
                                )
                            }
                        }
                        if (expand) {
                            if (completedIngredients.isNotEmpty()) {
                                completedIngredients.forEach { item ->
                                    key(item.id) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            setUpLines(
                                                item, viewModel, true, completedIngredients
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
    viewModel: () -> RecipeViewModel,
    isCompleted: Boolean,
    completedIngredients: List<Ingredient>?
) {
    val moveIngredientChoice by viewModel().moveIngredientChoice.collectAsState()
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
            .padding(start = 10.dp, top = 10.dp, end = 16.dp, bottom = 30.dp)
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
                text = item.name.substringBeforeLast(","),
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

            val focusRequester = remember { FocusRequester() }

            IngredientSettingOptions(item,
                viewModel = viewModel(),
                focusRequester = focusRequester,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .weight(1f),
                selectedIndex = moveIngredientChoice,
                showMessage = { ingredient, message ->
                    val name = ingredient.name.substringBeforeLast(",")
                    val spannableString = SpannableString(message)
                    val nameStart = message.indexOf(name)
                    val nameEnd = nameStart + name.length
                    spannableString.setSpan(
                        ForegroundColorSpan(MealPrepColor.orange.toArgb()), // Change color here
                        nameStart, nameEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
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