package com.example.mealprep.ui.groceries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealprep.Ingredient
import com.example.mealprep.data.model.Aisle
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.MealPrepTheme
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.android.material.color.MaterialColors

@Composable
fun AisleMenuChoice(
    viewModel: RecipeViewModel,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    notSetLabel: String? = null,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: Ingredient) -> Unit,
    chooseAisle: () -> Unit,
    selectedItemToString: (Ingredient) -> String = { it.toString() },
    drawItem: @Composable (String, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        CategoryDropdownMenuAisles(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },

    ) {
    var expanded by remember { mutableStateOf(false) }

    val listAisles = listOf(
        Aisle.FRUITS_AND_VEGETABLES.departmentName,
        Aisle.MEATS_AND_SEAFOOD.departmentName,
        Aisle.BREAD_AND_BAKERY.departmentName,
        Aisle.DAIRY_AND_EGGS.departmentName,
        Aisle.HERBS_AND_SPICES.departmentName,
        Aisle.BAKING.departmentName,
        Aisle.SNACKS.departmentName,
        Aisle.CANNED_FOODS.departmentName,
        Aisle.CONDIMENTS.departmentName,
        Aisle.PASTA_RICE.departmentName,
        Aisle.DRINKS.departmentName,
        Aisle.DIFFERENT_STORE.departmentName
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
                            if (aisle != null) {
                                drawItem(
                                    aisle,
                                    true,
                                    true,
                                ) {
//                            onItemSelected(index, item)
//                                    expanded = false
                                }
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
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

@Composable
fun CategoryDropdownMenu(
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