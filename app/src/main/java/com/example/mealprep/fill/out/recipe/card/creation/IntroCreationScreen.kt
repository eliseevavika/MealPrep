package com.example.mealprep.fill.out.recipe.card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.mealprep.ui.theme.fontFamilyForError
import java.util.regex.Pattern


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IntroCreationScreen(viewModel: RecipeCreationViewModel, focusRequester: FocusRequester) {
    val title = viewModel.title.collectAsState()
    val hours = viewModel.hours.collectAsState()
    val minutes = viewModel.minutes.collectAsState()
    val description = viewModel.description.collectAsState()
    val category = viewModel.category.collectAsState()
    val serves = viewModel.serves.collectAsState()
    val source = viewModel.source.collectAsState()

    val patternForTitle = remember { Regex("^\\s*$") }
    val patternForHoursAndMinutes = remember { Regex("^\\d+\$") }

    val isErrorTitle = viewModel.isErrorTitle.collectAsState()

    val isValidUrl = viewModel.isValidUrl.collectAsState()


    fun validateTitle(text: String) {
        viewModel.setIsErrorTitle(text.isEmpty() || text.matches(patternForTitle))
    }

    fun verifyUrl(url: String) {
        // Regular expression pattern to validate URLs
//        val urlPattern = "^(http(s)?:\\/\\/)?[\\w.-]+(\\.[\\w\\.-]+)+[\\S^\\\"]*$"

         val urlPattern: Pattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
        )
        viewModel.setIsValidUrl(url.matches(urlPattern.toRegex()))
    }

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
                modifier = Modifier.focusRequester(focusRequester),
                value = title.value,
                textStyle = TextStyle(color = MealPrepColor.black),
                onValueChange = {
                    viewModel.setRecipeName(it)
                    validateTitle(it)
                },
                isError = isErrorTitle.value,
                keyboardActions = KeyboardActions(onAny = { validateTitle(title.value) }),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MealPrepColor.white,
                    cursorColor = if (isErrorTitle.value) MealPrepColor.error else MealPrepColor.black,
                    focusedIndicatorColor = if (isErrorTitle.value) MealPrepColor.error else MealPrepColor.black,
                    unfocusedIndicatorColor = if (isErrorTitle.value) MealPrepColor.error else MealPrepColor.black,
                    focusedLabelColor = MealPrepColor.grey_800,
                    unfocusedLabelColor = MealPrepColor.grey_800,
                ),
                placeholder = {
                    Text(
                        text = "Give your recipe a name", fontFamily = fontFamilyForBodyB2
                    )
                })
            if (isErrorTitle.value) {
                Text(
                    text = "* This field is required",
                    color = MealPrepColor.error,
                    fontFamily = fontFamilyForError,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
            } else {
                Text(
                    text = "",
                    color = MealPrepColor.error,
                    fontFamily = fontFamilyForError,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                text = "Cook time",
                fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )

            Row {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(end = 10.dp)
                ) {
                    OutlinedTextField(
                        value = if (hours.value == 0) "" else hours.value.toString(),
                        textStyle = TextStyle(color = MealPrepColor.black),
                        onValueChange = {
                            if (it.isEmpty() || it.matches(patternForHoursAndMinutes) && it.length < 3) {
                                if (it != "") viewModel.setHours(it.toInt()) else viewModel.setHours(
                                    0
                                )
                            }
                            viewModel.setCookTime()
                        },
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
                        value = if (minutes.value == 0) "" else minutes.value.toString(),
                        textStyle = TextStyle(color = MealPrepColor.black),
                        onValueChange = {
                            if (it.isEmpty() || it.matches(patternForHoursAndMinutes) && it.length < 3) {
                                if (it != "") viewModel.setMinutes(it.toInt()) else viewModel.setMinutes(
                                    0
                                )
                            }
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
                RequestContentPermission(viewModel)
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
                modifier = Modifier.padding(top = 16.dp),
                text = "Source", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            TextField(
                value = source.value,
                textStyle = TextStyle(color = MealPrepColor.black),
                onValueChange = { newUrl ->
                    viewModel.setSource(newUrl)
                    verifyUrl(newUrl)
                },
                isError = !isValidUrl.value,
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
                        text = "Your recipe link", fontFamily = fontFamilyForBodyB2
                    )
                })

            if (!isValidUrl.value) {
                Text(
                    text = "* Not a valid URL",
                    color = MealPrepColor.error,
                    fontFamily = fontFamilyForError,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
            } else {
                Text(
                    text = "",
                    color = MealPrepColor.error,
                    fontFamily = fontFamilyForError,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Category", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )

            val listCategory = listOf("Main dishes", "Pastry", "Others")

            CategoryDropdownMenu(
                placeholder = "Select a category",
                items = listCategory,
                selectedIndex = listCategory.indexOf(category.value),
                onItemSelected = { index, _ -> viewModel.setCategory(listCategory[index]) },
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Serves",
                fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )

            val listServes = listOf("1", "2", "4", "6", "8")

            CategoryDropdownMenu(
                placeholder = "Select number of servings",
                items = listServes,
                selectedIndex = listServes.indexOf(serves.value),
                onItemSelected = { index, _ ->
                    viewModel.setServesCount(listServes[index])
                },
            )
            Spacer(modifier = Modifier.padding(vertical = 50.dp))
        }
    }
}

