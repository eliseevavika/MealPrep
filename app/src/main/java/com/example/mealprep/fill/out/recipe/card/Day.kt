package com.example.mealprep.fill.out.recipe.card


data class Day(
    val id: Int,
    val title: String
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