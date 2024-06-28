package com.example.mealprep.fill.out.recipe.card

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.groceries.ShoppingListSettingOptions
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.viewmodel.RecipeViewModel

@Composable
fun TopBarForGroceriesScreen(
    viewModel: () -> RecipeViewModel
) {
    val context = LocalContext.current

    val listGroceries =
        viewModel().ingredientsFromMealPlans.observeAsState(listOf()).value.sortedBy { it.first.aisle }
            .groupBy { it.first.aisle }
            .flatMap { (_, groupedList) -> groupedList.sortedBy { it.first.short_name } }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Groceries", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp
            )
        }
        Column(
            modifier = Modifier.wrapContentWidth(Alignment.End),
            horizontalAlignment = Alignment.End
        ) {
            ShoppingListSettingOptions(viewModel = viewModel(),
                makeCopy = {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val groceryNamesMain = listGroceries.map { it.first.name }
                    val titleMainGroceries = "Groceries:"

                    val joinedName = listOf(titleMainGroceries, "") + groceryNamesMain

                    val joinedIngredients = joinedName.joinToString(separator = "\n")

                    val clipData = ClipData.newPlainText(
                        "Shopping List", joinedIngredients
                    )
                    clipboardManager.setPrimaryClip(clipData)
                })
        }
    }
}