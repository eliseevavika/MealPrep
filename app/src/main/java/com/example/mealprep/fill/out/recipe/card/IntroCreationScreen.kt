package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.DishRepository
import com.example.mealprep.RecipesFeed
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2


@Composable
fun IntroCreationScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current.applicationContext
    Column {
        Row() {
            Box(){
                val textState = remember { mutableStateOf(TextFieldValue()) }
                Text(text = "Title",
                    fontFamily = fontFamilyForBodyB2,
                    fontSize = 14.sp,
                    color = MealPrepColor.grey_800,)
                
                TextField(value = textState.value, onValueChange = {textState.value = it}  )

                Text("The textfield has this text: " + textState.value.text)
            }
        }

    }
}