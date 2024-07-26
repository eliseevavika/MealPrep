package com.sliceup.mealprep.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import java.util.*

class SliceUpRepository(private val sliceUpDao: SliceUpDao) {
    var currentUserUID: String = Firebase.auth.currentUser?.uid.toString()

    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        listIngredients: List<Ingredient>?,
        listSteps: List<Step>?,
        currentUserUID: String
    ): Long = sliceUpDao.insertRecipeIngredientAndStepTransaction(
        recipe,
        listIngredients,
        listSteps,
        currentUserUID
    )

    suspend fun updateRecipeWithNewData(
        recipeId: Long,
        recipeWithUpdatedFields: Recipe,
        listIngredients: List<Ingredient>?,
        listSteps: List<Step>?,
        userUid: String
    ) = sliceUpDao.updateRecipeWithNewData(
        recipeId,
        recipeWithUpdatedFields,
        listIngredients,
        listSteps,
        userUid
    )

    fun getRecipeById(id: Long): Recipe {
        return sliceUpDao.getRecipeById(id)
    }

    fun getListOfIngredients(recipeId: Long): List<Ingredient> {
        return sliceUpDao.getListOfIngredients(recipeId)
    }

    fun getListOfSteps(recipeId: Long): List<Step> {
        return sliceUpDao.getListOfSteps(recipeId)
    }

    suspend fun insertRecipeAndMealPlanTransaction(dayId: Int, recipes: List<Recipe>?) {
        sliceUpDao.insertRecipeAndMealPlanTransaction(dayId, recipes)
    }

    suspend fun deleteRecipeAndMealPlanTransaction(dayId: Int) {
        sliceUpDao.deleteRecipeAndMealPlanTransaction(dayId)
    }

    suspend fun deleteAllExtraAddedIngredientsTransaction() {
        sliceUpDao.deleteAllExtraAddedIngredientsTransaction()
    }

    suspend fun makeIngredientComplete(ingredient: Ingredient) {
        sliceUpDao.makeIngredientComplete(ingredient)
    }

    suspend fun makeIngredientActing(ingredient: Ingredient) {
        sliceUpDao.makeIngredientActing(ingredient)
    }

    suspend fun insertExtraIngredientToDB(ingredient: Ingredient) =
        sliceUpDao.insertIngredient(ingredient)


    var allRecipes: LiveData<List<Recipe>> = sliceUpDao.getAllRecipes(currentUserUID)

    var recipesForSunday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(0, currentUserUID)

    var recipesForMonday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(1, currentUserUID)

    var recipesForTuesday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(2, currentUserUID)

    var recipesForWednesday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(3, currentUserUID)

    var recipesForThursday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(4, currentUserUID)

    var recipesForFriday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(5, currentUserUID)

    var recipesForSaturday: Flow<List<Recipe>> = sliceUpDao.getRecipesForTheDay(6, currentUserUID)

    private val _ingredientsFromMealPlansWithCount: LiveData<List<IngredientWithCount>> =
        sliceUpDao.getAllIngredientsFromMealPlansNotCompleted(currentUserUID)

    var ingredientsFromMealPlans: LiveData<List<Pair<Ingredient, Int>>> =
        _ingredientsFromMealPlansWithCount.map { ingredientWithCounts ->
            ingredientWithCounts.map { it.ingredient to it.recipe_count }
        }

    var listGroceriesForAnotherStore: LiveData<List<Ingredient>> =
        sliceUpDao.getAllIngredientsFromMealPlansNotCompletedAndForAnotherStore(currentUserUID)

    var oneWeekAgo = getOneWeekBefore()

    var completedIngredients: LiveData<List<Ingredient>> = sliceUpDao.getAllCompletedIngredients(
        currentUserUID, oneWeekAgo
    )

    fun getOneWeekBefore(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        return calendar.time
    }

    fun refreshDataForHome() {
        currentUserUID = Firebase.auth.currentUser?.uid.toString()
        allRecipes = sliceUpDao.getAllRecipes(currentUserUID)
    }

    fun refreshDataForMealPrep() {
        currentUserUID = Firebase.auth.currentUser?.uid.toString()

        recipesForSunday = sliceUpDao.getRecipesForTheDay(0, currentUserUID)

        recipesForMonday = sliceUpDao.getRecipesForTheDay(1, currentUserUID)

        recipesForTuesday = sliceUpDao.getRecipesForTheDay(2, currentUserUID)

        recipesForWednesday = sliceUpDao.getRecipesForTheDay(3, currentUserUID)

        recipesForThursday = sliceUpDao.getRecipesForTheDay(4, currentUserUID)

        recipesForFriday = sliceUpDao.getRecipesForTheDay(5, currentUserUID)

        recipesForSaturday = sliceUpDao.getRecipesForTheDay(6, currentUserUID)
    }

    fun refreshDataForGroceries() {
        currentUserUID = Firebase.auth.currentUser?.uid.toString()

        ingredientsFromMealPlans = _ingredientsFromMealPlansWithCount.map { ingredientWithCounts ->
            ingredientWithCounts.map { it.ingredient to it.recipe_count }
        }

        listGroceriesForAnotherStore =
            sliceUpDao.getAllIngredientsFromMealPlansNotCompletedAndForAnotherStore(currentUserUID)

        val oneWeekAgo = getOneWeekBefore()

        completedIngredients = sliceUpDao.getAllCompletedIngredients(
            currentUserUID,
            oneWeekAgo
        )
    }

    fun getAllIngredients(uid: String): List<Ingredient> {
        return sliceUpDao.getAllIngredients(uid)
    }

    fun getAllRecipesWithMealPlans(): List<RecipeWithMealPlan> {
        return sliceUpDao.getAllRecipesWithMealPlans()
    }

    fun getAllSteps(): List<Step> {
        return sliceUpDao.getAllSteps()
    }

    suspend fun clearDatabase() {
        sliceUpDao.clearRecipes()
        sliceUpDao.clearIngredients()
        sliceUpDao.clearRecipeWithMealPlan()
        sliceUpDao.clearSteps()
    }

    suspend fun insertAllRecipes(recipeData: List<Recipe>) {
        sliceUpDao.insertAllRecipes(recipeData)
    }

    suspend fun insertAllIngredients(ingredientData: List<Ingredient>) {
        sliceUpDao.insertAllIngredients(ingredientData)
    }

    suspend fun insertAllRecipeWithMealplanData(recipewithmealplanData: List<RecipeWithMealPlan>) {
        sliceUpDao.insertAllRecipeWithMealplanData(recipewithmealplanData)
    }

    suspend fun insertAllSteps(stepData: List<Step>) {
        sliceUpDao.insertAllSteps(stepData)
    }

    suspend fun updateRecipeImageFromFirebase(recipeId: Long, downloadUrl: Uri) {
        sliceUpDao.updateRecipePhoto(recipeId, downloadUrl.toString())
    }

    fun updateAisleForAllGroceries(
        ingredientId: Long,
        aisleNumber: Int,
        ingredientShortName: String
    ) {
        sliceUpDao.updateAisleForAllGroceries(ingredientId, aisleNumber, ingredientShortName)
    }

    fun updateAisleNumber(ingredientId: Long, aisleNumber: Int) {
        sliceUpDao.updateAisleNumber(ingredientId, aisleNumber)
    }

    suspend fun makeAllIgredientsActive(dayId: Int) {
        val recipeIds = sliceUpDao.getRecipeIdsForMealplan(dayId)
        for (recipeId in recipeIds) {
            sliceUpDao.updateIngredientsForRecipe(recipeId)
        }
    }

    suspend fun deleteTheRecipe(recipe: Recipe, userUid: String) {
        sliceUpDao.deleteTheRecipe(recipe)
    }
}