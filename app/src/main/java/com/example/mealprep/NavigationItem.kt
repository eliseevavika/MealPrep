package com.example.mealprep

import com.example.meaprep.R





sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.icon_home, "Home")
    object MealPrep : NavigationItem("mealprep", R.drawable.icon_mealprep, "MealPrep")
    object Groceries : NavigationItem("groceries", R.drawable.icon_groceries, "Groceries")
    object Settings : NavigationItem("settings", R.drawable.icon_settings, "Books")
}
