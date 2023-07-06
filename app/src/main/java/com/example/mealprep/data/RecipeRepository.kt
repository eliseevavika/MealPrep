package com.example.mealprep.data

import androidx.lifecycle.LiveData
import com.example.mealprep.*
import com.example.mealprep.data.model.Groceries
import com.example.mealprep.data.model.Steps
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {
    private val cache: MutableMap<Int, Flow<List<Recipe>>> = mutableMapOf()
    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        listIngredients: List<Groceries>?,
        listSteps: List<Steps>?
    ) {
        recipeDao.insertRecipeIngredientAndStepTransaction(recipe, listIngredients, listSteps)
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

    suspend fun deleteRecipeAndMealPlanTransaction(dayId: Int) {
        recipeDao.deleteRecipeAndMealPlanTransaction(dayId)
    }

    suspend fun makeIngredientComplete(ingredient: Ingredient) {
        recipeDao.makeIngredientComplete(ingredient)
    }

    suspend fun makeIngredientActing(ingredient: Ingredient) {
        recipeDao.makeIngredientActing(ingredient)
    }

    suspend fun insertExtraIngredientToDB(ingredient: Ingredient) {
        recipeDao.insertIngredient(ingredient)
    }

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()

    val recipesForSunday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(0)

    val recipesForMonday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(1)

    val recipesForTuesday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(2)

    val recipesForWednesday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(3)

    val recipesForThursday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(4)

    val recipesForFriday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(5)

    val recipesForSaturday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(6)

    val ingredientsFromMealPlans: LiveData<List<Ingredient>> =
        recipeDao.getAllIngredientsFromMealPlansNotCompleted()

    val completedIngredients: LiveData<List<Ingredient>> = recipeDao.getAllCompletedIngredients()

//    fun getRecipesForTheDay(dayId: Int): Flow<List<Recipe>> {
//        // Check if the data is already cached
//        val cachedData = cache[dayId]
//        if (cachedData != null) {
//            return cachedData
//        }
//        // Fetch the data from the database
//        val newData = recipeDao.getRecipesForTheDay(dayId)
//        // Cache the data for future use
//        cache[dayId] = newData
//
//        return newData
//    }
}