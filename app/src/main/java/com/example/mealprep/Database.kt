package com.example.mealprep

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.example.mealprep.fill.out.recipe.card.Groceries
import java.io.ByteArrayOutputStream
import java.util.*


@Entity(tableName = "user")
data class UserRoom(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,
    val email: String,
    val password: String
)

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?,
    var photo: String?,
    @ColumnInfo(name = "cook_time") val cook_time: Int?,
    @ColumnInfo(name = "serves") val serves: Int?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "user_id") val user_id: Int = 1,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "creation_date") val creation_date: Date,
)

@Entity(
    tableName = "ingredient",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["id"],
        childColumns = ["recipe_id"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true) @NonNull val id: Int = 0,
    var name: String,
    val completed: Boolean = false,
    val recipe_id: Long?
)

data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ingredientsList: List<Ingredient>
)

@Entity(tableName = "mealplan")
data class MealplanRoom(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val user_id: Int,
//    val recipes: List<Int>?
)


@Entity(tableName = "instruction")
data class InstructionRoom(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val description: String,
    val recipe_id: Int
)

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): LiveData<List<UserRoom>>
}

//    @Insert
//    fun insertAll(vararg menuItems: MenuItemRoom)
//
//    @Query("SELECT (SELECT COUNT(*) FROM MenuItemRoom) == 0")
//    fun isEmpty(): Boolean

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(recipe: Recipe): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Transaction
    suspend fun insertRecipeAndIngredientTransaction(
        recipe: Recipe,
        ingredients: List<Groceries>?
    ) {
        // Anything inside this method runs in a single transaction.
        val list = mutableListOf<Ingredient>()

        val recipeId = insert(recipe)

        ingredients?.forEach { ingredient ->
            val item = Ingredient(
                name = ingredient.name,
                completed = false,
                recipe_id = recipeId
            )
            list.add(item)
        }
        insertIngredients(list)
    }

    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipeWithIngredients(recipeId: Int): List<RecipeWithIngredients>

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM Recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Update
    suspend fun update(recipe: Recipe)
}

@Dao
interface IngredientDao {
    @Insert
    suspend fun insertIngredient(ingredient: Ingredient)

//    @Insert
//    suspend fun insertIngredientsForRecipe(ingredients: List<Ingredient>)
//    @Query("SELECT * FROM Recipe")
//    fun getRecipeWithIngredients(): List<RecipeWithIngredientslist>
}

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM Mealplan")
    fun getAll(): LiveData<List<MealplanRoom>>
}

@Database(
    entities = [UserRoom::class, Recipe::class, MealplanRoom::class, Ingredient::class, InstructionRoom::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): RecipeDao

    abstract fun getIngredientDao(): IngredientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
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

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun convertBitmapToString(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun converterStringToBitmap(str: String): Bitmap? {
        return try {
            val encodeByte = Base64.getDecoder().decode(str)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

