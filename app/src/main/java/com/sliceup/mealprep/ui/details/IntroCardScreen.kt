package com.sliceup.mealprep.ui.details

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceup.mealprep.data.Recipe
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2

@Composable
fun IntroCardScreen(viewModel: () -> RecipeViewModel) {
    val recipe = viewModel().returnedRecipe.observeAsState().value

    Box(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                text = "Description", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            Text(
                text = "${recipe?.description}", fontFamily = fontFamilyForBodyB2,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 10.dp),
                text = "Source", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            recipe?.let { SourceOfRecipe(it) }
        }
    }
}

@Composable
fun SourceOfRecipe(recipe: Recipe) {
    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val mAnnotatedLinkString = buildAnnotatedString {
        val mStr = recipe.source
        append(mStr.toString())
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = 0, end = mStr?.length ?: 0
        )
        addStringAnnotation(
            tag = "URL",
            annotation = mStr.toString(),
            start = 0,
            end = mStr?.length ?: 0
        )
    }

    val mUriHandler = LocalUriHandler.current

    val context = LocalContext.current

    Column {
        ClickableText(
            text = mAnnotatedLinkString,
            onClick = {
                try {
                    mAnnotatedLinkString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            mUriHandler.openUri(stringAnnotation.item)
                        }
                } catch (e: Exception) {
                    showMessage(context, "Not a valid URL")
                }
            }
        )
    }
}

