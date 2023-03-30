package com.example.mealprep.fill.out.recipe.card.mealplanning

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopAppBarMealPlanning() {

//
//    val previousMonday: LocalDate = LocalDate.now(ZoneId.of("America/Montreal"))
//        .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
//


    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth(60f),
        verticalAlignment = Alignment.CenterVertically
    ) {

//
//
//        Kalendar(
//            kalendarType = KalendarType.Oceanic(),
//
//            takeMeToDate = previousMonday.toKotlinLocalDate(),
//
//
//        )


    }


}