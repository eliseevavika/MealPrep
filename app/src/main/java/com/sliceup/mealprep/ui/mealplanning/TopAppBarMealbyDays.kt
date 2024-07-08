package com.sliceup.mealprep.ui.mealplanning

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1

@Composable
fun TopAppBarMealbyDays() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth(0.6f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Edit plan", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp)
    }
}
