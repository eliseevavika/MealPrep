package com.example.mealprep.ui.groceries

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
    onItemSelected: (index: Int, item: String) -> Unit,
    showMessage: (Ingredient, String) -> Unit,
    drawItem: @Composable (String, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        CategoryDropdown(
            text = item,
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },

    ) {
    var expanded by remember { mutableStateOf(false) }

    val listChoices = listOf("Move to another store", "Move to another aisle:")

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
                                onItemSelected(1, listChoices[1])
                            }
                            AisleMenuChoice(viewModel = viewModel,
                                focusRequester = focusRequester,
                                modifier = Modifier.focusRequester(focusRequester),
                                selectedIndex = newAisleChoice,
                                onItemSelected = { index, _ ->
                                    viewModel.setIngredientSettingChoice(index)
                                },
                                chooseAisle = {

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