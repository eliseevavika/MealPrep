package com.example.mealprep.fill.out.recipe.card.mealplanning

import com.example.mealprep.Recipe
import kotlinx.coroutines.flow.Flow

object RecipeCache {
    private var cachedRecipes: Flow<List<Recipe>>? = null

    fun getRecipes(): Flow<List<Recipe>>? {
        return cachedRecipes
    }

    fun cacheRecipes(recipes: Flow<List<Recipe>>) {
        cachedRecipes = recipes
    }
}