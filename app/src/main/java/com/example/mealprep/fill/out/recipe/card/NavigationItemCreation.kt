package com.example.mealprep.fill.out.recipe.card

import com.example.meaprep.R





sealed class NavigationItemCreation(var route: String, var title: String) {
    object Intro : NavigationItemCreation("intro",  "Intro")
    object Ingredients : NavigationItemCreation("ingredients",  "Ingredients")
    object Steps : NavigationItemCreation("steps", "Steps")
}
