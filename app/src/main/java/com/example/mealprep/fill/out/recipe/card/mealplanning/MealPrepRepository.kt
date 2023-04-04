package com.example.mealprep.fill.out.recipe.card.mealplanning

import com.example.littlelemon.Dish


object MealPrepRepository {
    val mealPreps = listOf(
        MealPrepByDay(
            0,
            ArrayList()
        ),
        MealPrepByDay(
            1,
            ArrayList()
        ),
        MealPrepByDay(
            2,
            ArrayList()
        ),
        MealPrepByDay(
            3,
            ArrayList()
        ),
        MealPrepByDay(
            4,
            ArrayList()
        ),
        MealPrepByDay(
            5,
            ArrayList()
        ),
        MealPrepByDay(
            6,
            ArrayList()
        ),
    )

    fun getMealPrepByDay(id: Int) = mealPreps.firstOrNull { it.id == id }



}

data class MealPrepByDay(
    val id: Int,
    var dishesForMealPrep: List<Dish>
)


