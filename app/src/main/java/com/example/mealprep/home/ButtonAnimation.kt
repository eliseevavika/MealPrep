package com.example.mealprep.home

import android.service.autofill.OnClickAction
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import kotlinx.coroutines.launch
import org.w3c.dom.Text

@Composable
fun ButtonAnimation(
    animationDuration: Int = 100,
    scaleDown: Float = 0.9f,
    text : String
) {

    val interactionSource = MutableInteractionSource()

    val coroutineScope = rememberCoroutineScope()

    val scale = remember {
        Animatable(1f)
    }

    Box(
        modifier = Modifier
            .scale(scale = scale.value)
            .background(
                color = MealPrepColor.white,
                shape = RoundedCornerShape(size = 12f)
            )
            .clickable(interactionSource = interactionSource, indication = null) {
                coroutineScope.launch {
                    scale.animateTo(
                        scaleDown,
                        animationSpec = tween(animationDuration),
                    )
                    scale.animateTo(
                        1f,
                        animationSpec = tween(animationDuration),
                    )
                }
            }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 36.dp, vertical = 12.dp),
            color = MealPrepColor.orange,
            fontFamily = fontFamilyForBodyB2,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
