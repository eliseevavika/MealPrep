package com.sliceup.mealprep.ui.groceries

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sliceup.mealprep.data.Ingredient
import com.sliceup.mealprep.data.model.Aisle
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.MealPrepTheme
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.google.android.material.color.MaterialColors

@Composable
fun IngredientSettingOptions(
    ingredient: Ingredient,
    isCompleted: Boolean,
    viewModel: RecipeViewModel,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showMessage: (Ingredient, String) -> Unit,
    showMessageForAisleUpdate: (Ingredient, Aisle) -> Unit,
    drawItem: @Composable (String, () -> Unit) -> Unit = { item, onClick ->
        CategoryDropdown(
            text = item,
            onClick = onClick,
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    if (isCompleted) {
        // don't need to show menu for completed items
        return
    }

    val listChoices = listOf("Move to another aisle:")

    val newAisleChoice by viewModel.newAisleChoice.collectAsState()

    val context = LocalContext.current

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            MealPrepTheme() {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        Row() {
                            drawItem(
                                listChoices[0],
                            ) {}
                            AisleMenuChoice(ingredient = ingredient,
                                viewModel = viewModel,
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
                                        }.setNegativeButton("No") { dialog, _ ->
                                            expanded = false
                                            dialog.dismiss()
                                        }.show()
                                })
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
    onClick: () -> Unit,
) {
    val contentColor = when {
        else -> MaterialTheme.colors.onSurface.copy(alpha = MaterialColors.ALPHA_FULL)
    }
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(true) { onClick() }
            .padding(16.dp)) {
            Text(
                text = text, fontFamily = fontFamilyForBodyB1
            )
        }
    }
}