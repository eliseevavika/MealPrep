package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlelemon.Dish

@Composable
fun TopAppBarMealbyDays() { Row(horizontalArrangement = Arrangement.Center,
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .fillMaxWidth(60f),
    verticalAlignment = Alignment.CenterVertically) {


    Text(text = "Select recipes", style = MaterialTheme.typography.body1, fontSize = 20.sp)
}
}
