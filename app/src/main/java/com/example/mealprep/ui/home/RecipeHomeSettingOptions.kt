package com.example.mealprep.ui.home

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
import androidx.navigation.NavController
import com.example.mealprep.Recipe
import com.example.mealprep.ui.navigation.DishDetails
import com.example.mealprep.ui.navigation.GroceriesAddition
import com.example.mealprep.ui.navigation.RecipeCreation
import com.example.mealprep.ui.theme.MealPrepTheme
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.android.material.color.MaterialColors

@Composable
fun RecipeHomeSettingOptions(
    recipe: Recipe,
    viewModel: RecipeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    drawItem: @Composable (String, () -> Unit) -> Unit = { item, onClick ->
        CategoryDropdownForRecipeHomeDetails(
            text = item, onClick = onClick
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    val listChoices = listOf("Edit recipe", "Delete recipe")

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
                        drawItem(
                            listChoices[0],
                        ) {
                            expanded = false
                            viewModel.getAllStateDataForRecipe(recipe)
                            navController.navigate(RecipeCreation.route)
                        }

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        drawItem(
                            listChoices[1]
                        ) {
                            expanded = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdownForRecipeHomeDetails(
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
