package com.example.mealprep.ui.groceries

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mealprep.Ingredient
import com.example.mealprep.data.model.Aisle
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.MealPrepTheme
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.android.material.color.MaterialColors

@Composable
fun IngredientSettingOptions(
    ingredient: Ingredient,
    viewModel: RecipeViewModel,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedIndex: Int = -1,
    showMessage: (Ingredient, String) -> Unit,
    showMessageForAisleUpdate: (Ingredient, Aisle) -> Unit,
    drawItem: @Composable (String, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        CategoryDropdown(
            text = item,
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    val listChoices = listOf("Move to another store", "Move to another aisle:")

    val newAisleChoice by viewModel.newAisleChoice.collectAsState()

    val context = LocalContext.current

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .width(200.dp)
    ) {
        IconButton(onClick = {

        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            MealPrepTheme() {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                ) {
                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        drawItem(
                            listChoices[0],
                            selectedIndex == 0,
                            true,
                        ) {
                            expanded = false
                            val name = ingredient.name.substringBeforeLast(",")

                            showMessage(
                                ingredient,
                                "Item ${name} moved to the different store section"
                            )

                            viewModel.updateAisleNumber(ingredient, Aisle.DIFFERENT_STORE.value)
                        }

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        Row() {
                            drawItem(
                                listChoices[1],
                                selectedIndex == 1,
                                true,
                            ) {
                            }
                            AisleMenuChoice(ingredient = ingredient,
                                viewModel = viewModel,
                                modifier = Modifier.focusRequester(focusRequester),
                                selectedIndex = newAisleChoice,
                                onItemSelected = { aisle ->
                                    viewModel.setNewAsleChoice(aisle)
                                },
                                showDialog = { ingredient, aisle, message ->
                                    val ingredientName = ingredient.name.substringBeforeLast(",")
                                    val spannableMessage = SpannableString(message)

                                    val ingredientStart = message.indexOf(ingredientName)
                                    if (ingredientStart != -1) {
                                        spannableMessage.setSpan(
                                            ForegroundColorSpan(MealPrepColor.orange.toArgb()),
                                            ingredientStart,
                                            ingredientStart + ingredientName.length,
                                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                                        )
                                    }
                                    val aisleStart = message.indexOf(aisle.departmentName!!)
                                    if (aisleStart != -1) {
                                        spannableMessage.setSpan(
                                            ForegroundColorSpan(MealPrepColor.orange.toArgb()),
                                            aisleStart,
                                            aisleStart + aisle.departmentName.length,
                                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                                        )
                                    }
                                    android.app.AlertDialog.Builder(context)
                                        .setMessage(spannableMessage)
                                        .setPositiveButton("Yes") { dialog, _ ->
                                            expanded = false
                                            dialog.dismiss()
                                            viewModel.updateAisleNumber(ingredient, aisle.value)
                                            showMessageForAisleUpdate(ingredient, aisle)
                                        }
                                        .setNegativeButton("No") { dialog, _ ->
                                            expanded = false
                                            dialog.dismiss()
                                        }
                                        .show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryDropdown(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colors.onSurface.copy(alpha = MaterialColors.ALPHA_DISABLED)
        selected -> MealPrepColor.orange.copy(alpha = MaterialColors.ALPHA_FULL)
        else -> MaterialTheme.colors.onSurface.copy(alpha = MaterialColors.ALPHA_FULL)
    }
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .padding(16.dp)) {
            Text(
                text = text, fontFamily = fontFamilyForBodyB1
            )
        }
    }
}