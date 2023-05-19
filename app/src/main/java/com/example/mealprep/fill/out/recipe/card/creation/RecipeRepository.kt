package com.example.mealprep.fill.out.recipe.card.creation

import androidx.lifecycle.LiveData
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.Groceries

class RecipeRepository(private val recipeDao: RecipeDao) {
    suspend fun insertRecipeAndIngredientTransaction(recipe: Recipe, list: List<Groceries>?) {
        recipeDao.insertRecipeAndIngredientTransaction(recipe, list)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    // on below line we are creating a update method for
    // updating our note from database.
    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()
}

