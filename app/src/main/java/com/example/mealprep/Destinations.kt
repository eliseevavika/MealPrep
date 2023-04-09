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

object MealPrepForSpecificDay : Destinations {
    override val route = "mealPrepForSpecificDay"
    const val argDayId = "dayId"
}

object GroceriesAddition : Destinations {
    override val route = "groceriesAddition"
}

object RecipeCreation : Destinations {
    override val route = "recipeCreation"
}