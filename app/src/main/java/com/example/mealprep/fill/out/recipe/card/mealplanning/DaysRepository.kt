package com.example.mealprep.fill.out.recipe.card.mealplanning

import com.example.littlelemon.DishRepository

object DaysRepository {
    val days = listOf {
        Day(0, DayOfWeek.Sunday)
        Day(1, DayOfWeek.Monday)
        Day(2, DayOfWeek.Tuesday)
        Day(3, DayOfWeek.Wednesday)
        Day(4, DayOfWeek.Thursday)
        Day(5, DayOfWeek.Friday)
        Day(6, DayOfWeek.Saturday)

//        fun getDay(id: Int) = DaysRepository.days {day -> day.id == id }
    }
}

data class Day(
    val id: Int,
    val name: DayOfWeek,
) {
    val getId: Int
        get() = id

    val getName: DayOfWeek
        get() = name
}
