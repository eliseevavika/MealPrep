package com.sliceuptest.mealprep.ui.mealplanning

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceuptest.mealprep.data.Recipe
import com.sliceuptest.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceuptest.mealprep.viewmodel.RecipeViewModel

@Composable
fun TopAppBarMealPlanning(
    viewModel: () -> RecipeViewModel,
    recipesForSunday: List<Recipe>,
    recipesForMonday: List<Recipe>,
    recipesForTuesday: List<Recipe>,
    recipesForWednesday: List<Recipe>,
    recipesForThursday: List<Recipe>,
    recipesForFriday: List<Recipe>,
    recipesForSaturday: List<Recipe>,
    showMessage: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Meal planning", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp)
        }
        Column(
            modifier = Modifier.wrapContentWidth(align = Alignment.End),
            horizontalAlignment = Alignment.End
        ) {
            MealPrepSettingOptions(viewModel = viewModel(),
                makeCopyWithLinks = {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                    val listMealPlanWithLinks = viewModel().makeCopyMealPlansWithLinks(
                        recipesForSunday,
                        recipesForMonday,
                        recipesForTuesday,
                        recipesForWednesday,
                        recipesForThursday,
                        recipesForFriday,
                        recipesForSaturday
                    )

                    val joinedNameForAllMealPlans = mutableListOf<String>()

                    listMealPlanWithLinks.forEach { mealPlan ->
                        val joinedNameForSpecificMealPlan =
                            viewModel().makeJoinedNameForSpecificMealPlan(mealPlan)
                        joinedNameForAllMealPlans.addAll(joinedNameForSpecificMealPlan)
                    }

                    if (joinedNameForAllMealPlans.isNotEmpty()) {
                        val joinedRecipes = joinedNameForAllMealPlans.joinToString(separator = "\n")

                        val clipData = ClipData.newPlainText(
                            "Meal planning", joinedRecipes
                        )
                        clipboardManager.setPrimaryClip(clipData)
                    } else {
                        showMessage()
                    }

                })
        }
    }
}


