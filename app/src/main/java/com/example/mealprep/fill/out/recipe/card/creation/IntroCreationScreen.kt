package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroCreationScreen(viewModel: RecipeCreationViewModel) {
    val title = viewModel.title.collectAsState()
    val hours = viewModel.hours.collectAsState()
    val minutes = viewModel.minutes.collectAsState()
    val description = viewModel.description.collectAsState()
    val categoryIndex = viewModel.categoryIndex.observeAsState().value
    val servesIndex = viewModel.servesIndex.observeAsState().value

    Box(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Title", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            TextField(
                value = title.value,
                textStyle = TextStyle(color = MealPrepColor.black),
                onValueChange = viewModel::setRecipeName,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MealPrepColor.white,
                    cursorColor = MealPrepColor.black,
                    focusedIndicatorColor = MealPrepColor.black,
                    unfocusedIndicatorColor = MealPrepColor.black,
                    focusedLabelColor = MealPrepColor.grey_800,
                    unfocusedLabelColor = MealPrepColor.grey_800
                ),
                placeholder = {
                    Text(
                        text = "Give your recipe a name", fontFamily = fontFamilyForBodyB2
                    )
                })

            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                text = "Cook time",
                fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )

            val maxCharHours = 2
            val maxCharMinutes = 3

            Row {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(end = 10.dp)
                ) {
                    OutlinedTextField(
                        value = hours.value.toString(),
                        textStyle = TextStyle(color = MealPrepColor.black),
                        onValueChange = {
                            viewModel.setHours(it.toInt())
                            viewModel.setCookTime()
                        },
//                        onValueChange = {
//                            if (it.length <= maxCharHours)
                        label = { Text("hours", fontFamily = fontFamilyForBodyB2) },
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
                        value = minutes.value.toString(),
                        textStyle = TextStyle(color = MealPrepColor.black),
                        onValueChange = {
                            viewModel.setMinutes(it.toInt())
                            viewModel.setCookTime()
                        },
                        label = { Text("minutes", fontFamily = fontFamilyForBodyB2) },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                RequestContentPermission()
            }


            Text(
                text = "Description", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            TextField(
                value = description.value,
                textStyle = TextStyle(color = MealPrepColor.black),
                onValueChange =
                viewModel::setDescription,

                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MealPrepColor.white,
                    cursorColor = MealPrepColor.black,
                    focusedIndicatorColor = MealPrepColor.black,
                    unfocusedIndicatorColor = MealPrepColor.black,
                    focusedLabelColor = MealPrepColor.grey_800,
                    unfocusedLabelColor = MealPrepColor.grey_800
                ),
                placeholder = {
                    Text(
                        text = "Give your recipe a description", fontFamily = fontFamilyForBodyB2
                    )
                })

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                text = "Category", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )

            var selectedCategoryIndex by remember { mutableStateOf(-1) }

            CategoryDropdownMenu(
                placeholder = "Select a category",
                items = listOf("Main dishes", "Pastry", "Others"),
                selectedIndex = selectedCategoryIndex,
                onItemSelected = { index, _ -> selectedCategoryIndex = index },
            )

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                text = "Serve",
                fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )

            var selectedServesIndex by remember { mutableStateOf(-1) }
            CategoryDropdownMenu(
                placeholder = "Select number of servings",
                items = listOf("1", "2", "4", "6", "8"),
                selectedIndex = selectedServesIndex,
                onItemSelected = { index, _ -> selectedServesIndex = index },
            )

            Spacer(modifier = Modifier.padding(vertical = 50.dp))
        }
    }
}


@Preview
@Composable
fun IntroCreationScreenPreview() {
//    IntroCreationScreen(viewModel)
}