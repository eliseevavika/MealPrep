package com.example.mealprep.fill.out.recipe.card

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.groceries.ShoppingListSettingOptions
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.viewmodel.RecipeViewModel

@Composable
fun TopBarForGroceriesScreen(
    viewModel: () -> RecipeViewModel
) {
    val context = LocalContext.current

    val listGroceries =
        viewModel().ingredientsFromMealPlans.observeAsState(listOf()).value.sortedBy { it.aisle }
            .groupBy { it.aisle }
            .flatMap { (_, groupedList) -> groupedList.sortedBy { it.short_name } }
    val listGroceriesForAnotherStore =
        viewModel().listGroceriesForAnotherStore.observeAsState(listOf()).value.sortedBy { it.aisle }
            .groupBy { it.aisle }
            .flatMap { (_, groupedList) -> groupedList.sortedBy { it.short_name } }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp
        )
        Text(
            text = "Groceries", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp
        )

        ShoppingListSettingOptions(viewModel = viewModel(),
            modifier = Modifier.fillMaxWidth(0.2f),
            makeCopy = {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val groceryNamesMain = listGroceries.map { it.name }
                val groceryNamesAnother = listGroceriesForAnotherStore.map { it.name }
                val titleMainGroceries = "Main groceries:"
                val titleAnotherStore = "Another store:"
                val joinedName = listOf(titleMainGroceries, "") + groceryNamesMain + listOf(
                    "", titleAnotherStore, ""
                ) + groceryNamesAnother
                val joinedIngredientsForAnotherStore = joinedName.joinToString(separator = "\n")

                val clipData = ClipData.newPlainText(
                    "Shopping List", joinedIngredientsForAnotherStore
                )
                clipboardManager.setPrimaryClip(clipData)
            })
    }
}