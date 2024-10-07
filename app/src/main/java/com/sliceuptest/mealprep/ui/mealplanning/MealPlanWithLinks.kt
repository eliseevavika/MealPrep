package com.sliceuptest.mealprep.ui.mealplanning

data class MealPlanWithLinks(
    val dayOfWeek: Int,
    val recipesWithLinks: List<RecipeWithLink>,
)

data class RecipeWithLink(
    val recipeName: String,
    val recipeLink: String?
)