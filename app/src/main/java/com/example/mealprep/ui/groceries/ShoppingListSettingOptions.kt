package com.example.mealprep.ui.groceries

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mealprep.ui.theme.MealPrepTheme
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.android.material.color.MaterialColors

@Composable
fun ShoppingListSettingOptions(
    viewModel: RecipeViewModel,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedIndex: Int = -1,
    makeCopy: () -> Unit,
    drawItem: @Composable (String, () -> Unit) -> Unit = { item, onClick ->
        CategoryDropdownForShoppingList(
            text = item, onClick = onClick
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    val listChoices = listOf("Make a copy", "Mark all complete")

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
                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        drawItem(
                            listChoices[0],
                        ) {
                            expanded = false
                            makeCopy()
                        }

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        drawItem(
                            listChoices[1]
                        ) {
                            expanded = false
                            viewModel.markAllComplete()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryDropdownForShoppingList(
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