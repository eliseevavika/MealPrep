package com.example.mealprep.fill.out.recipe.card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarRecipeCreationForm(
    navController: NavHostController,
    viewModel: RecipeCreationViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
            .fillMaxWidth(60f),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = {
            navController.popBackStack("home", inclusive = false)
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
        Text(
            text = "Recipe Form", fontFamily = fontFamilyForBodyB1,
            fontSize = 20.sp
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.transparent),
            interactionSource = NoRippleInteractionSource(),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            shape = RoundedCornerShape(50),
            modifier = Modifier.bounceClick(),
            onClick = {
                if (viewModel.isRquiredDataEntered()) {
                    viewModel.addNewRecipe()
                }else{

                }
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