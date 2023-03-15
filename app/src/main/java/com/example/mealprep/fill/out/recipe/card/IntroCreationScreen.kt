package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.DishRepository
import com.example.mealprep.RecipesFeed
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2


@Composable
fun IntroCreationScreen() {

    Box(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
        Column() {
            val titleState = remember { mutableStateOf(TextFieldValue()) }

            Text(
                text = "Title", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            TextField(
                value = titleState.value,
                textStyle = TextStyle(color = MealPrepColor.black),
                onValueChange = { titleState.value = it },

                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MealPrepColor.white,
                    cursorColor = MealPrepColor.black,
                    focusedIndicatorColor = MealPrepColor.black,
                    unfocusedIndicatorColor = MealPrepColor.black,
                    focusedLabelColor = MealPrepColor.grey_800,
                    unfocusedLabelColor = MealPrepColor.grey_800
                ),
                placeholder = { Text(text = "Give your recipe a name") })



            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Cook time",
                fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )


            val maxCharHours = 2
            val maxCharMinutes = 3
            val hoursState = remember { mutableStateOf(TextFieldValue()) }
            val minutesState = remember { mutableStateOf(TextFieldValue()) }
            Row() {
                Box(modifier = Modifier.width(120.dp)) {
                    OutlinedTextField(
                        value = hoursState.value,
                        textStyle = TextStyle(color = MealPrepColor.black),
                        onValueChange = {
                            if (it.text.length <= maxCharHours) hoursState.value = it
                        },
                        label = { Text("hours") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MealPrepColor.white,
                            cursorColor = MealPrepColor.black,
                            focusedIndicatorColor = MealPrepColor.black,
                            unfocusedIndicatorColor = MealPrepColor.black,
                            focusedLabelColor = MealPrepColor.grey_800,
                            unfocusedLabelColor = MealPrepColor.grey_800
                        ),
                    )
                }
                Box(modifier = Modifier.width(120.dp)) {
                    OutlinedTextField(
                        value = minutesState.value,
                        textStyle = TextStyle(color = MealPrepColor.black),
                        onValueChange = {
                            if (it.text.length <= maxCharMinutes) minutesState.value = it
                        },
                        label = { Text("minutes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MealPrepColor.white,
                            cursorColor = MealPrepColor.black,
                            focusedIndicatorColor = MealPrepColor.black,
                            unfocusedIndicatorColor = MealPrepColor.black,
                            focusedLabelColor = MealPrepColor.grey_800,
                            unfocusedLabelColor = MealPrepColor.grey_800
                        ),
                    )
                }
            }
            RequestContentPermission()
        }


    }

}


@Preview
@Composable
fun IntroCreationScreenPreview() {
    IntroCreationScreen()
}


