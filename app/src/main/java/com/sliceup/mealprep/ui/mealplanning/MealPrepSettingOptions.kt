package com.sliceup.mealprep.ui.mealplanning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sliceup.mealprep.ui.theme.MealPrepTheme
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.google.android.material.color.MaterialColors

@Composable
fun MealPrepSettingOptions(
    viewModel: RecipeViewModel,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedIndex: Int = -1,
    makeCopyWithLinks: () -> Unit,
    drawItem: @Composable (String, () -> Unit) -> Unit = { item, onClick ->
        CategoryDropdownForMealPrep(
            text = item, onClick = onClick
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    val listChoices = listOf("Copy menu for the whole week", "Reset all meal plans")

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
                            makeCopyWithLinks()
                        }

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        drawItem(
                            listChoices[1]
                        ) {
                            expanded = false
                            viewModel.resetAllMealPlansForTheWeek()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdownForMealPrep(
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