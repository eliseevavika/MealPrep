package com.example.mealprep

interface Destinations {
    val route: String
}

object Home : Destinations {
    override val route = "home"
}

object MealPrep : Destinations {
    override val route = "mealprep"
}

object Groceries : Destinations {
    override val route = "groceries"
}

object Settings : Destinations {
    override val route = "settings"
}

object DishDetails : Destinations {
    override val route = "menu"
    const val argDishId = "dishId"
}