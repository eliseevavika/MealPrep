package com.example.mealprep.fill.out.recipe.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlelemon.Dish
import com.example.littlelemon.DishRepository
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2

@Composable
fun IntroCardScreen(id: Int) {
    val dish = requireNotNull(DishRepository.getDish(id))

    Box(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                text = "Description", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            Text(
                text = "${dish.description}", fontFamily = fontFamilyForBodyB2,
                fontSize = 16.sp
            )
            Text(modifier = Modifier.padding(top = 16.dp, bottom = 10.dp),
                text = "Source", fontFamily = fontFamilyForBodyB1,
                fontSize = 20.sp,
            )
            SourceOfRecipe(dish)


        }
    }

}

@Composable
fun SourceOfRecipe(dish: Dish) {
    val mAnnotatedLinkString = buildAnnotatedString {

        val mStr = dish.source

        append(mStr)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = 0, end = mStr.length
        )

        // attach a string annotation that
        // stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = mStr,
            start = 0,
            end = mStr.length
        )

    }

    // UriHandler parse and opens URI inside
    // AnnotatedString Item in Browse
    val mUriHandler = LocalUriHandler.current

    Column(
    ) {

        // ???? Clickable text returns position of text
        // that is clicked in onClick callback
        ClickableText(
            text = mAnnotatedLinkString,
            onClick = {
                mAnnotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        mUriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}

@Composable
fun ExtraIntroCardScreen() {
    IntroCardScreen(1)
}

@Preview
@Composable
fun IntroCardScreenPreview() {
    ExtraIntroCardScreen()
}