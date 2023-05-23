package com.example.mealprep.fill.out.recipe.card.creation

import androidx.lifecycle.LiveData
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.Groceries
import com.example.mealprep.fill.out.recipe.card.Steps

class RecipeRepository(private val recipeDao: RecipeDao) {
    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        listIngredients: List<Groceries>?,
        listSteps: List<Steps>?
    ) {
        recipeDao.insertRecipeIngredientAndStepTransaction(recipe, listIngredients, listSteps)
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

    fun getRecipeById(id: Long): Recipe {
      return recipeDao.getReipeById(id)
    }

    fun getListOfIngredients(recipeId: Long): List<Ingredient> {
       return recipeDao.getListOfIngredients(recipeId)
    }

    fun getListOfSteps(recipeId: Long): List<Step> {
        return recipeDao.getListOfSteps(recipeId)
    }

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()
}

