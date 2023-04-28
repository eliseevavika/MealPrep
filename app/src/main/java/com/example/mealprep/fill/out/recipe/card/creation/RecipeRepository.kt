package com.example.mealprep.fill.out.recipe.card.creation

import androidx.lifecycle.LiveData
import com.example.mealprep.Recipe
import com.example.mealprep.RecipeDao

class RecipeRepository(private val recipeDao: RecipeDao) {
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
    suspend fun delete(recipe: Recipe){
        recipeDao.delete(recipe)
    }

    // on below line we are creating a update method for
    // updating our note from database.
    suspend fun update(recipe: Recipe){
        recipeDao.update(recipe)
    }


    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()

}