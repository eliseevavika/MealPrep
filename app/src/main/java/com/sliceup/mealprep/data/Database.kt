package com.sliceup.mealprep.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import kotlinx.coroutines.flow.Flow
import java.util.*


@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val recipe_id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String?,
    var photo: String?,
    @ColumnInfo(name = "cook_time") var cook_time: Int?,
    @ColumnInfo(name = "serves") var serves: Int?,
    @ColumnInfo(name = "source") var source: String?,
    @ColumnInfo(name = "user_uid") var user_uid: String,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "creation_date") val creation_date: Date,
)

data class IngredientWithCount(
    @Embedded val ingredient: Ingredient,
    val recipe_count: Int
)

@Entity(
    tableName = "ingredient", foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["recipe_id"],
        childColumns = ["recipe_id"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var completed: Boolean = false,
    var completion_date: Date?,
    val recipe_id: Long?,
    val aisle: Int,
    val short_name: String,
    @ColumnInfo(name = "user_uid") var user_uid: String,
)

@Entity(tableName = "recipewithmealplan", primaryKeys = ["recipe_id", "mealplan_id"])
data class RecipeWithMealPlan(
    @ColumnInfo(name = "recipe_id") val recipe_id: Long,
    @ColumnInfo(name = "mealplan_id") val mealplan_id: Int
)

@Entity(tableName = "step")
data class Step(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var description: String,
    val recipe_id: Long
)

@Dao
interface SliceUpDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(recipe: Recipe): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Insert
    suspend fun insertIngredient(ingredient: Ingredient): Long

    @Insert
    suspend fun insertSteps(steps: List<Step>)

    @Transaction
    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        ingredients: List<Ingredient>?,
        steps: List<Step>?,
        currentUserUID: String
    ): Long {
        // Anything inside this method runs in a single transaction.
        val listIngredients = mutableListOf<Ingredient>()

        val listSteps = mutableListOf<Step>()

        val recipeId = insert(recipe)

        ingredients?.forEach { ingredient ->
            val item = Ingredient(
                name = ingredient.name,
                completed = false,
                completion_date = null,
                recipe_id = recipeId,
                aisle = 0,
                short_name = ingredient.name,
                user_uid = currentUserUID
            )
            listIngredients.add(item)
        }

        steps?.forEach { step ->
            val item = Step(
                description = step.description, recipe_id = recipeId
            )
            listSteps.add(item)
        }
        insertIngredients(listIngredients)
        insertSteps(listSteps)
        return recipeId
    }

    @Transaction
    suspend fun insertUpdatedRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        ingredients: List<Ingredient>?,
        steps: List<Step>?,
        currentUserUID: String
    ): Long {
        // Anything inside this method runs in a single transaction.
        val listIngredients = mutableListOf<Ingredient>()

        val listSteps = mutableListOf<Step>()

        updateRecipe(recipe)

        ingredients?.forEach { ingredient ->
            val item = Ingredient(
                name = ingredient.name,
                completed = false,
                completion_date = null,
                recipe_id = recipe.recipe_id,
                aisle = 0,
                short_name = ingredient.name,
                user_uid = currentUserUID
            )
            listIngredients.add(item)
        }

        steps?.forEach { step ->
            val item = Step(
                description = step.description, recipe_id = recipe.recipe_id
            )
            listSteps.add(item)
        }
        insertIngredients(listIngredients)
        insertSteps(listSteps)
        return recipe.recipe_id
    }


    @Transaction
    suspend fun updateRecipeWithNewData(
        recipeId: Long,
        recipeWithUpdatedFields: Recipe,
        listIngredients: List<Ingredient>?,
        listSteps: List<Step>?,
        userUid: String
    ) {
        val recipe = getRecipeById(recipeId)

        recipe.name = recipeWithUpdatedFields.name
        recipe.description = recipeWithUpdatedFields.description
        recipe.photo = recipeWithUpdatedFields.photo
        recipe.cook_time = recipeWithUpdatedFields.cook_time
        recipe.serves = recipeWithUpdatedFields.serves
        recipe.source = recipeWithUpdatedFields.source
        recipe.category = recipeWithUpdatedFields.category

        deleteAllIngredientsForRecipe(recipe.recipe_id)
        deleteAllStepsForRecipe(recipe.recipe_id)
        insertUpdatedRecipeIngredientAndStepTransaction(recipe, listIngredients,listSteps, userUid)
    }
    @Transaction
    suspend fun deleteTheRecipe(recipe: Recipe) {
        delete(recipe)
        deleteAllIngredientsForRecipe(recipe.recipe_id)
        deleteAllStepsForRecipe(recipe.recipe_id)
        deleteMealPlansForTheRecipe(recipe.recipe_id)
    }

    @Query("DELETE FROM RecipeWithMealPlan WHERE recipe_id = :recipeId")
    fun deleteMealPlansForTheRecipe(recipeId: Long)

    @Query("DELETE FROM Ingredient WHERE recipe_id = :recipeId")
     fun deleteAllIngredientsForRecipe(recipeId: Long)

    @Query("DELETE FROM Step WHERE recipe_id = :recipeId")
    fun deleteAllStepsForRecipe(recipeId: Long)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM Recipe WHERE user_uid = :uid")
    fun getAllRecipes(uid: String): LiveData<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE recipe_id = :id")
    fun getRecipeById(id: Long): Recipe

    @Query("SELECT * FROM Ingredient WHERE recipe_id = :recipeId")
    fun getListOfIngredients(recipeId: Long): List<Ingredient>

    @Query("SELECT * FROM Step WHERE recipe_id = :recipeId")
    fun getListOfSteps(recipeId: Long): List<Step>

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipeWithMealPlan(recipeWithMealPlan: RecipeWithMealPlan)

    @Transaction
    suspend fun insertRecipeAndMealPlanTransaction(dayId: Int, recipes: List<Recipe>?) {
        recipes?.forEach { recipe ->
            addRecipeWithMealPlan(RecipeWithMealPlan(recipe.recipe_id, dayId))
        }
    }

    @Query("SELECT * FROM Ingredient WHERE id = :ingredientId")
    suspend fun getIngredientById(ingredientId: Long): Ingredient?

    @Query("DELETE FROM recipewithmealplan WHERE mealplan_id = :dayId")
    fun deleteRecipeWithMealPlan(dayId: Int)

    @Query("DELETE FROM ingredient WHERE recipe_id IS NULL")
    fun deleteAllExtraAddedIngredients()

    @Transaction
    suspend fun deleteRecipeAndMealPlanTransaction(dayId: Int) {
        deleteRecipeWithMealPlan(dayId)
    }

    @Transaction
    suspend fun deleteAllExtraAddedIngredientsTransaction() {
        deleteAllExtraAddedIngredients()
    }

    @Transaction
    @Query("SELECT Recipe.*  FROM Recipe JOIN recipewithmealplan ON Recipe.recipe_id = recipewithmealplan.recipe_id WHERE recipewithmealplan.mealplan_id = :dayId AND Recipe.user_uid = :currentUserUID")
    fun getRecipesForTheDay(dayId: Int, currentUserUID: String?): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT Ingredient.*, COUNT(recipewithmealplan.mealplan_id) AS recipe_count FROM Ingredient INNER JOIN recipewithmealplan ON Ingredient.recipe_id = recipewithmealplan.recipe_id INNER JOIN Recipe ON Recipe.recipe_id = Ingredient.recipe_id WHERE NOT Ingredient.completed AND Ingredient.aisle != 13 AND Recipe.user_uid = :currentUserUID GROUP BY Ingredient.id UNION ALL SELECT Ingredient.*, 1 AS recipe_count FROM Ingredient  WHERE recipe_id IS NULL AND NOT completed AND aisle != 13 AND user_uid = :currentUserUID")
    fun getAllIngredientsFromMealPlansNotCompleted(currentUserUID: String): LiveData<List<IngredientWithCount>>

    @Transaction
    @Query("SELECT Ingredient.* FROM Ingredient INNER JOIN recipewithmealplan ON Ingredient.recipe_id = recipewithmealplan.recipe_id INNER JOIN Recipe ON Recipe.recipe_id = Ingredient.recipe_id WHERE NOT Ingredient.completed AND Ingredient.aisle = 13 AND Recipe.user_uid = :currentUserUID  UNION SELECT * FROM Ingredient WHERE recipe_id IS NULL AND NOT completed  AND aisle = 13 AND user_uid = :currentUserUID")
    fun getAllIngredientsFromMealPlansNotCompletedAndForAnotherStore(currentUserUID: String): LiveData<List<Ingredient>>

    @Transaction
    @Query("SELECT Ingredient.* FROM Ingredient INNER JOIN recipewithmealplan ON Ingredient.recipe_id = recipewithmealplan.recipe_id INNER JOIN Recipe ON Recipe.recipe_id = Ingredient.recipe_id WHERE Ingredient.completed AND Recipe.user_uid = :currentUserUID  AND completion_date > :oneWeekAgo UNION SELECT * FROM Ingredient WHERE recipe_id IS NULL AND completed AND user_uid = :currentUserUID AND completion_date > :oneWeekAgo")
    fun getAllCompletedIngredients(
        currentUserUID: String,
        oneWeekAgo: Date
    ): LiveData<List<Ingredient>>

    @Query("DELETE FROM Recipe")
    suspend fun clearRecipes()

    @Query("DELETE FROM Ingredient")
    suspend fun clearIngredients()

    @Query("DELETE FROM recipewithmealplan")
    suspend fun clearRecipeWithMealPlan()

    @Query("DELETE FROM Step")
    suspend fun clearSteps()

    @Transaction
    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Transaction
    suspend fun makeIngredientComplete(ingredient: Ingredient) {
        val ingredient = getIngredientById(ingredient.id)
        if (ingredient != null) {
            ingredient.completed = true
            ingredient.completion_date = Calendar.getInstance().time
            updateIngredient(ingredient)
        }
    }

    @Transaction
    suspend fun makeIngredientActing(ingredient: Ingredient) {
        val ingredient = getIngredientById(ingredient.id)
        if (ingredient != null) {
            ingredient.completed = false
            ingredient.completion_date = null
            updateIngredient(ingredient)
        }
    }

    @Query("SELECT * FROM Ingredient WHERE user_uid = :uid")
    fun getAllIngredients(uid: String): List<Ingredient>

    @Query("SELECT * FROM recipewithmealplan")
    fun getAllRecipesWithMealPlans(): List<RecipeWithMealPlan>

    @Query("SELECT * FROM step")
    fun getAllSteps(): List<Step>

    @Insert
    suspend fun insertAllRecipes(recipeData: List<Recipe>)

    @Insert
    suspend fun insertAllIngredients(ingredientData: List<Ingredient>)

    @Insert
    suspend fun insertAllRecipeWithMealplanData(recipewithmealplanData: List<RecipeWithMealPlan>)

    @Insert
    suspend fun insertAllSteps(stepData: List<Step>)

    @Query("UPDATE Recipe SET photo = :newPhoto WHERE recipe_id = :recipeId")
    suspend fun updateRecipePhoto(recipeId: Long, newPhoto: String)

    @Query("UPDATE Ingredient SET aisle = :aisleNumber, short_name = :ingredientShortName WHERE id = :ingredientId")
    fun updateAisleForAllGroceries(
        ingredientId: Long,
        aisleNumber: Int,
        ingredientShortName: String
    )

    @Query("UPDATE Ingredient SET aisle = :aisleNumber WHERE id = :ingredientId")
    fun updateAisleNumber(ingredientId: Long, aisleNumber: Int)

    @Query("SELECT recipe_id FROM recipewithmealplan WHERE mealplan_id = :mealplanId")
    fun getRecipeIdsForMealplan(mealplanId: Int): List<Long>

    @Query("UPDATE Ingredient SET completed = 0, completion_date = NULL WHERE recipe_id = :recipeId")
    suspend fun updateIngredientsForRecipe(recipeId: Long)
}

@Dao
interface IngredientDao {
}

@Dao
interface MealPlanDao {
}

@Dao
interface RecipeWithMealPlanDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecipeWithMealPlan(join: RecipeWithMealPlan)
}

@Database(
    entities = [Recipe::class, Ingredient::class, Step::class, RecipeWithMealPlan::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): SliceUpDao

    abstract fun getIngredientDao(): IngredientDao

    abstract fun getMealPlanDao(): MealPlanDao

    abstract fun getRecipeWithMealPlanDao(): RecipeWithMealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "app_database"
                )
                    .fallbackToDestructiveMigration()
//                    //TODO (fallbackToDestructiveMigration)
//                    // If you don’t want to provide migrations and you specifically want your database to be cleared when you upgrade the version, call fallbackToDestructiveMigration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}


