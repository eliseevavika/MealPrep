package com.example.mealprep

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.room.*
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

@Entity(tableName = "mealplan")
data class MealplanRoom(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val user_id: Int,
    val recipes: List<Int>?
)

@Entity(tableName = "ingredient")
data class IngredientRoom(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val completed: Boolean,
    val recipe_id: Int?
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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)


    @Query("SELECT * FROM Recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>


    @Update
    suspend fun update(recipe: Recipe)
}

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM Mealplan")
    fun getAll(): LiveData<List<MealplanRoom>>
}

@Database(
    entities = [UserRoom::class, Recipe::class, MealplanRoom::class, IngredientRoom::class, InstructionRoom::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): RecipeDao

//    abstract fun getUserDao(): UserDao

//    abstract fun getMealPlanDao(): MealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
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
                // return instance
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

    @TypeConverter
    fun fromListIntToString(intList: List<Int>): String = intList.toString()

    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val result = ArrayList<Int>()
        val split = stringList.replace("[", "").replace("]", "").replace(" ", "").split(",")
        for (n in split) {
            try {
                result.add(n.toInt())
            } catch (e: Exception) {

            }
        }
        return result
    }


}

