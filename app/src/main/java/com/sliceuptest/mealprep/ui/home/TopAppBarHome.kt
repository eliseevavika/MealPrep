package com.sliceuptest.mealprep.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sliceuptest.mealprep.ui.theme.fontFamilyForBodyB1


@Composable
fun TopAppBarHome() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Recipes", fontFamily = fontFamilyForBodyB1, fontSize = 20.sp)
    }
}