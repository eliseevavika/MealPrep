package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.theme.fontFamilyForBodyB1

@Composable
fun TopAppBarMealbyDays() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth(60f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Edit plan", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp)
    }
}
