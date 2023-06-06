package com.example.mealprep.fill.out.recipe.card.creation

import androidx.lifecycle.LiveData
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.Day
import com.example.mealprep.fill.out.recipe.card.Groceries
import com.example.mealprep.fill.out.recipe.card.Steps
import com.example.mealprep.fill.out.recipe.card.days
import com.example.mealprep.fill.out.recipe.card.mealplanning.RecipeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class RecipeRepository(private val recipeDao: RecipeDao) {
    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        listIngredients: List<Groceries>?,
        listSteps: List<Steps>?
    ) {
        recipeDao.insertRecipeIngredientAndStepTransaction(recipe, listIngredients, listSteps)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    fun getRecipeById(id: Long): Recipe {
        return recipeDao.getRecipeById(id)
    }

    fun getListOfIngredients(recipeId: Long): List<Ingredient> {
        return recipeDao.getListOfIngredients(recipeId)
    }

    fun getListOfSteps(recipeId: Long): List<Step> {
        return recipeDao.getListOfSteps(recipeId)
    }

    suspend fun insertRecipeAndMealPlanTransaction(dayId: Int, recipes: List<Recipe>?) {
        recipeDao.insertRecipeAndMealPlanTransaction(dayId, recipes)
    }

    fun getRecipesForTheDay(dayId: Int): Flow<List<Recipe>> {
        return recipeDao.getRecipesForTheDay(dayId)
    }

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()

    val recipesForSunday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(0)

    val recipesForMonday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(1)

    val recipesForTuesday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(2)

    val recipesForWednesday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(3)

    val recipesForThursday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(4)

    val recipesForFriday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(5)

    val recipesForSaturday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(6)
}