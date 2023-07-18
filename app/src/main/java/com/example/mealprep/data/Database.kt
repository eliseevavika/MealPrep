package com.example.mealprep

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import kotlinx.coroutines.flow.Flow
import java.util.*


@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val recipe_id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?,
    val photo: String?,
    @ColumnInfo(name = "cook_time") val cook_time: Int?,
    @ColumnInfo(name = "serves") val serves: Int?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "user_uid") val user_uid: String,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "creation_date") val creation_date: Date,
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
    val recipe_id: Long?,
    @ColumnInfo(name = "user_uid") val user_uid: String,
)

@Entity(tableName = "recipewithmealplan", primaryKeys = ["recipe_id", "mealplan_id"])
data class RecipeWithMealPlan(
    @ColumnInfo(name = "recipe_id") val recipe_id: Long,
    @ColumnInfo(name = "mealplan_id") val mealplan_id: Int
)

@Entity(tableName = "step")
data class Step(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val recipe_id: Long
)

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(recipe: Recipe): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Insert
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert
    suspend fun insertSteps(steps: List<Step>)

    @Transaction
    suspend fun insertRecipeIngredientAndStepTransaction(
        recipe: Recipe,
        ingredients: List<com.example.mealprep.data.model.Groceries>?,
        steps: List<com.example.mealprep.data.model.Steps>?,
        currentUserUID: String
    ) {
        // Anything inside this method runs in a single transaction.
        val listIngredients = mutableListOf<Ingredient>()

        val listSteps = mutableListOf<Step>()

        val recipeId = insert(recipe)

        ingredients?.forEach { ingredient ->
            val item = Ingredient(
                name = ingredient.name, completed = false, recipe_id = recipeId, user_uid = currentUserUID
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
    }

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
    suspend fun update(recipe: Recipe)

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

    @Transaction
    suspend fun deleteRecipeAndMealPlanTransaction(dayId: Int) {
        deleteRecipeWithMealPlan(dayId)
    }

    @Transaction
    @Query("SELECT Recipe.*  FROM Recipe JOIN recipewithmealplan ON Recipe.recipe_id = recipewithmealplan.recipe_id WHERE recipewithmealplan.mealplan_id = :dayId AND Recipe.user_uid = :currentUserUID")
    fun getRecipesForTheDay(dayId: Int, currentUserUID: String?): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT Ingredient.* FROM Ingredient INNER JOIN recipewithmealplan ON Ingredient.recipe_id = recipewithmealplan.recipe_id INNER JOIN Recipe ON Recipe.recipe_id = Ingredient.recipe_id WHERE NOT Ingredient.completed AND Recipe.user_uid = :currentUserUID UNION SELECT * FROM Ingredient WHERE recipe_id IS NULL AND NOT completed AND user_uid = :currentUserUID")
    fun getAllIngredientsFromMealPlansNotCompleted(currentUserUID: String): LiveData<List<Ingredient>>

    @Transaction
    @Query("SELECT Ingredient.* FROM Ingredient INNER JOIN recipewithmealplan ON Ingredient.recipe_id = recipewithmealplan.recipe_id INNER JOIN Recipe ON Recipe.recipe_id = Ingredient.recipe_id WHERE Ingredient.completed AND Recipe.user_uid = :currentUserUID UNION SELECT * FROM Ingredient WHERE recipe_id IS NULL AND completed AND user_uid = :currentUserUID")
    fun getAllCompletedIngredients(currentUserUID: String): LiveData<List<Ingredient>>

    @Transaction
    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Transaction
    suspend fun makeIngredientComplete(ingredient: Ingredient) {
        val ingredient = getIngredientById(ingredient.id)
        if (ingredient != null) {
            ingredient.completed = true
            updateIngredient(ingredient)
        }
    }

    @Transaction
    suspend fun makeIngredientActing(ingredient: Ingredient) {
        val ingredient = getIngredientById(ingredient.id)
        if (ingredient != null) {
            ingredient.completed = false
            updateIngredient(ingredient)
        }
    }
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
    abstract fun getRecipeDao(): RecipeDao

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
                ).fallbackToDestructiveMigration()
                    //TODO (fallbackToDestructiveMigration)
                    // If you don’t want to provide migrations and you specifically want your database to be cleared when you upgrade the version, call fallbackToDestructiveMigration
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


