package com.sliceup.mealprep.ui.groceries

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
import androidx.compose.ui.graphics.Color
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
fun AisleMenuChoice(
    ingredient: Ingredient,
    viewModel: RecipeViewModel,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int) -> Unit,
    showDialog: (ingredient: Ingredient, aisle: Aisle, message: String) -> Unit,
    drawItem: @Composable (String, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        CategoryDropdownMenuAisles(
            text = item,
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    val listAisles = listOf(
        Aisle.FRUITS_AND_VEGETABLES,
        Aisle.MEATS_AND_SEAFOOD,
        Aisle.BREAD_AND_BAKERY,
        Aisle.DAIRY_AND_EGGS,
        Aisle.HERBS_AND_SPICES,
        Aisle.BAKING,
        Aisle.SNACKS,
        Aisle.CANNED_FOODS,
        Aisle.CONDIMENTS,
        Aisle.PASTA_RICE,
        Aisle.DRINKS
    )

    val newAisleChoice by viewModel.newAisleChoice.collectAsState()

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
                        listAisles.forEach { aisle ->
                            if (aisle.departmentName != null) {
                                drawItem(
                                    aisle.departmentName,
                                    newAisleChoice == aisle.value,
                                    true,
                                ) {
                                    onItemSelected(newAisleChoice)
                                    expanded = false

                                    val ingredientName = ingredient.name.substringBeforeLast(",")

                                    showDialog(
                                        ingredient,
                                        aisle,
                                        "Do you want to move ${ingredientName} to ${aisle.departmentName} aisle?"
                                    )
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdownMenuAisles(
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
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                fontFamily = fontFamilyForBodyB1
            )
        }
    }
}