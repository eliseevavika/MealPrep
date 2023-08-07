package com.example.mealprep.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.mealprep.*
import com.example.mealprep.data.model.Groceries
import com.example.mealprep.data.model.Steps
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {
    var currentUserUID: String = Firebase.auth.currentUser?.uid.toString()

    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        listIngredients: List<Groceries>?,
        listSteps: List<Steps>?,
        currentUserUID: String
    ): Long = recipeDao.insertRecipeIngredientAndStepTransaction(
        recipe,
        listIngredients,
        listSteps,
        currentUserUID
    )

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

    var allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes(currentUserUID)

    var recipesForSunday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(0, currentUserUID)

    var recipesForMonday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(1, currentUserUID)

    var recipesForTuesday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(2, currentUserUID)

    var recipesForWednesday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(3, currentUserUID)

    var recipesForThursday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(4, currentUserUID)

    var recipesForFriday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(5, currentUserUID)

    var recipesForSaturday: Flow<List<Recipe>> = recipeDao.getRecipesForTheDay(6, currentUserUID)

    var ingredientsFromMealPlans: LiveData<List<Ingredient>> =
        recipeDao.getAllIngredientsFromMealPlansNotCompleted(currentUserUID)

    var completedIngredients: LiveData<List<Ingredient>> = recipeDao.getAllCompletedIngredients(
        currentUserUID
    )

    fun refreshDataForHome() {
        currentUserUID = Firebase.auth.currentUser?.uid.toString()
        allRecipes = recipeDao.getAllRecipes(currentUserUID)
    }

    fun refreshDataForMealPrep() {
        currentUserUID = Firebase.auth.currentUser?.uid.toString()

        recipesForSunday = recipeDao.getRecipesForTheDay(0, currentUserUID)

        recipesForMonday = recipeDao.getRecipesForTheDay(1, currentUserUID)

        recipesForTuesday = recipeDao.getRecipesForTheDay(2, currentUserUID)

        recipesForWednesday = recipeDao.getRecipesForTheDay(3, currentUserUID)

        recipesForThursday = recipeDao.getRecipesForTheDay(4, currentUserUID)

        recipesForFriday = recipeDao.getRecipesForTheDay(5, currentUserUID)

        recipesForSaturday = recipeDao.getRecipesForTheDay(6, currentUserUID)
    }

    fun refreshDataForGroceries() {
        currentUserUID = Firebase.auth.currentUser?.uid.toString()

        ingredientsFromMealPlans =
            recipeDao.getAllIngredientsFromMealPlansNotCompleted(currentUserUID)

        completedIngredients = recipeDao.getAllCompletedIngredients(currentUserUID)
    }

    fun getAllIngredients(uid: String): List<Ingredient> {
        return recipeDao.getAllIngredients(uid)
    }

    fun getAllRecipesWithMealPlans(): List<RecipeWithMealPlan> {
        return recipeDao.getAllRecipesWithMealPlans()
    }

    fun getAllSteps(): List<Step> {
        return recipeDao.getAllSteps()
    }

    suspend fun clearDatabase() {
        recipeDao.clearRecipes()
        recipeDao.clearIngredients()
        recipeDao.clearRecipeWithMealPlan()
        recipeDao.clearSteps()
    }

    suspend fun insertAllRecipes(recipeData: List<Recipe>) {
        recipeDao.insertAllRecipes(recipeData)
    }

    suspend fun insertAllIngredients(ingredientData: List<Ingredient>) {
        recipeDao.insertAllIngredients(ingredientData)
    }

    suspend fun insertAllRecipeWithMealplanData(recipewithmealplanData: List<RecipeWithMealPlan>) {
        recipeDao.insertAllRecipeWithMealplanData(recipewithmealplanData)
    }

    suspend fun insertAllSteps(stepData: List<Step>) {
        recipeDao.insertAllSteps(stepData)
    }

    suspend fun updateRecipeImageFromFirebase(recipeId: Long, downloadUrl: Uri) {
        recipeDao.updateRecipePhoto(recipeId, downloadUrl.toString())
    }

    fun updateAisleForAllGroceries(
        ingredientId: Long,
        aisleNumber: Int,
        ingredientShortName: String
    ) {
        recipeDao.updateAisleForAllGroceries(ingredientId, aisleNumber, ingredientShortName)
    }
}