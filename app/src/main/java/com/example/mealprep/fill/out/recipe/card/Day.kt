package com.example.mealprep.fill.out.recipe.card

import com.example.mealprep.fill.out.recipe.card.mealplanning.DayOfWeek


data class Day(
    val id: Int,
    var title: String
)

val days = listOf(
    Day(0, "Sunday"),
    Day(1, "Monday"),
    Day(2, "Tuesday"),
    Day(3, "Wednesday"),
    Day(4, "Thursday"),
    Day(5, "Friday"),
    Day(6, "Saturday")
)