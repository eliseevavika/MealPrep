package com.sliceup.mealprep.ui.groceries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sliceup.mealprep.ui.modifiers.NoRippleInteractionSource
import com.sliceup.mealprep.ui.modifiers.bounceClick
import com.sliceup.mealprep.ui.navigation.Groceries
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2

@Composable
fun TopBarGroceriesAdditionForm(
    viewModel: () -> RecipeViewModel, navController: () -> NavHostController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
            .fillMaxWidth(0.6f),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = {
            //TODO make back button handler
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
        Text(text = "Extra ingredients", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp)

        Button(colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.transparent),
            interactionSource = NoRippleInteractionSource(),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            shape = RoundedCornerShape(50),
            modifier = Modifier.bounceClick(),
            onClick = {
                viewModel().addExtraGroceriesToTheDB()
                navController().navigate(Groceries.route)
            }) {
            Text(
                text = "Save",
                fontFamily = fontFamilyForBodyB2,
                fontSize = 20.sp,
                color = MealPrepColor.orange
            )
        }
    }
}