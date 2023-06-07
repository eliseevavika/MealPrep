package com.example.mealprep.fill.out.recipe.card.creation

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.mealprep.*
import com.example.mealprep.fill.out.recipe.card.Groceries
import com.example.mealprep.fill.out.recipe.card.Steps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class RecipeCreationViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeRepository: RecipeRepository

    var allRecipes: LiveData<List<Recipe>>

    var recipesForSunday: LiveData<List<Recipe>>
    var recipesForMonday: LiveData<List<Recipe>>
    var recipesForTuesday: LiveData<List<Recipe>>
    var recipesForWednesday: LiveData<List<Recipe>>
    var recipesForThursday: LiveData<List<Recipe>>
    var recipesForFriday: LiveData<List<Recipe>>
    var recipesForSaturday: LiveData<List<Recipe>>

    val returnedRecipe = MutableLiveData<Recipe>()

    val returnedListIngredient = MutableLiveData<List<Ingredient>>()

    val returnedListSteps = MutableLiveData<List<Step>>()

    init {
        val recipeDao = AppDatabase.getDatabase(application).getRecipeDao()

        recipeRepository = RecipeRepository(recipeDao)

        allRecipes = recipeRepository.allRecipes

        recipesForSunday = recipeRepository.recipesForSunday
        recipesForMonday = recipeRepository.recipesForMonday
        recipesForTuesday = recipeRepository.recipesForTuesday
        recipesForWednesday = recipeRepository.recipesForWednesday
        recipesForThursday = recipeRepository.recipesForThursday
        recipesForFriday = recipeRepository.recipesForFriday
        recipesForSaturday = recipeRepository.recipesForSaturday
    }

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _day = MutableStateFlow(-1)
    val day = _day.asStateFlow()

    private val _hours = MutableStateFlow(0)
    val hours = _hours.asStateFlow()

    private val _chosenDay = MutableStateFlow(0)
    val chosenDay = _chosenDay.asStateFlow()

    private val _minutes = MutableStateFlow(0)
    val minutes = _minutes.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _photo = MutableStateFlow("")
    val photo = _photo.asStateFlow()

    private val _uri = MutableStateFlow<Uri?>(null)
    val uri = _uri.asStateFlow()

    private var _cook_time = MutableLiveData<Int>()
    val cook_time: LiveData<Int>
        get() = _cook_time

    private val _serves = MutableStateFlow("")
    val serves = _serves.asStateFlow()

    private val _source = MutableStateFlow("")
    val source = _source.asStateFlow()

    private val _category = MutableStateFlow("")
    val category = _category.asStateFlow()

    private val _chosenTabIndex = MutableStateFlow(0)
    val chosenTabIndex = _chosenTabIndex.asStateFlow()

    private var _listIngredients = MutableLiveData<List<Groceries>>()
    private var _last_id: Int = -1

    val listIngredients: LiveData<List<Groceries>>
        get() = _listIngredients

    private val _isErrorTitle = MutableStateFlow(false)
    val isErrorTitle = _isErrorTitle.asStateFlow()

    private val _isValidUrl = MutableStateFlow(true)
    val isValidUrl = _isValidUrl.asStateFlow()

    fun performQueryIngredients(
        ingredientName: String
    ) {
        val id = _last_id + 1
        _last_id = id

        if (ingredientName.isNotEmpty()) {
            val item = Groceries(id, ingredientName)

            _listIngredients.value = _listIngredients.value?.plus(item) ?: listOf(item)
        }
    }

    fun removeElementIngredients(
        item: Groceries
    ) {
        _listIngredients.value = _listIngredients.value?.filter { it != item }
    }

    fun setNameIngredients(
        item: Groceries,
        input: String
    ) {
        val undatedItem = Groceries(item.id, input)

        _listIngredients.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.name = undatedItem.name
            }
        }
    }

    private var _listSteps = MutableLiveData<List<Steps>>()
    private var _last__id_steps: Int = -1

    val listSteps: LiveData<List<Steps>>
        get() = _listSteps

    fun performQuerySteps(
        ingredientName: String
    ) {
        val id = _last__id_steps + 1
        _last__id_steps = id
        val number = 1
        if (ingredientName.isNotEmpty()) {
            val item = Steps(id, number, ingredientName)

            _listSteps.value = _listSteps.value?.plus(item) ?: listOf(item)
        }
    }

    fun removeElementSteps(
        item: Steps
    ) {
        _listSteps.value = _listSteps.value?.filter { it != item }
    }

    fun setNameSteps(
        item: Steps,
        input: String
    ) {
        val undatedItem = Steps(item.id, 1, input)

        _listSteps.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.description = undatedItem.description
            }
        }
    }

    fun setRecipeName(title: String?) {
        if (title != null) {
            _title.value = title
        }
    }

    fun setHours(hours: Int?) {
        if (hours != null) {
            _hours.value = hours
        }
    }

    fun setMinutes(minutes: Int?) {
        if (minutes != null) {
            _minutes.value = minutes
        }
    }

    fun setCookTime() {
        val time = _minutes.value?.let { _hours?.value?.times(60)?.plus(it) }
        _cook_time.value = time
    }

    fun setDescription(description: String?) {
        _description.value = description.toString()
    }

    fun setServesCount(count: String?) {
        if (count != null) {
            _serves.value = count
        }
    }

    fun setSource(link: String?) {
        if (link != null) {
            _source.value = link
        }
    }

    fun setCategory(category: String?) {
        if (category != null) {
            _category.value = category
        }
    }

    fun setPhoto(str: String) {
        _photo.value = str

    }
//    fun verifyPermissions(): Boolean? {
//
//        // This will return the current Status
//        val permissionExternalMemory =
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
//            val STORAGE_PERMISSIONS = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            // If permission not granted then ask for permission real time.
//            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 1)
//            return false
//        }
//        return true
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImage(image: Bitmap, storageDir: File, imageFileName: String) {
        var successDirCreated = false
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir()
        }
        if (successDirCreated) {
            val imageFile = File(storageDir, imageFileName)
            val savedImagePath: String = imageFile.getAbsolutePath()
            try {
                val resized = Bitmap.createScaledBitmap(
                    image,
                    (image.width * 0.8).toInt(),
                    (image.height * 0.8).toInt(),
                    true
                )
                val fOut: OutputStream = FileOutputStream(imageFile)
                resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                _photo.value = Converters().convertBitmapToString(resized)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isRquiredDataEntered(): Boolean {
        if (_title.value != "") {
            return true
        }
        return false
    }

    fun addNewRecipe() {
        val recipe = Recipe(
            name = _title.value,
            description = _description.value,
            photo = _photo.value,
            cook_time = _cook_time.value,
            serves = if (_serves.value == "") 0 else _serves.value.toInt(),
            source = _source.value,
            user_id = 1,
            category = _category.value,
            creation_date = Calendar.getInstance().time
        )
        addRecipe(recipe)
    }

    fun addNewMealPlan() {
        if (_chosenDay.value == 0) {
            _listChosenMeals.value = _listChosenMealsForSunday.value
        } else if (_chosenDay.value == 1) {
            _listChosenMeals.value = _listChosenMealsForMonday.value
        } else if (_chosenDay.value == 2) {
            _listChosenMeals.value = _listChosenMealsForTuesday.value
        } else if (_chosenDay.value == 3) {
            _listChosenMeals.value = _listChosenMealsForWednesday.value
        } else if (_chosenDay.value == 4) {
            _listChosenMeals.value = _listChosenMealsForThursday.value
        } else if (_chosenDay.value == 5) {
            _listChosenMeals.value = _listChosenMealsForFriday.value
        } else if (_chosenDay.value == 6) {
            _listChosenMeals.value = _listChosenMealsForSaturday.value
        } else {
            _listChosenMeals.value = listOf()
        }
        addMealPlan(_chosenDay.value)
    }

    fun addRecipe(recipe: Recipe) =
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.insertRecipeIngredientAndStepTransaction(
                recipe,
                _listIngredients.value,
                _listSteps.value
            )
            emptyLiveData()
        }

    fun addMealPlan(dayId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.deleteRecipeAndMealPlanTransaction(dayId)
            recipeRepository.insertRecipeAndMealPlanTransaction(dayId, _listChosenMeals.value)
        }

    fun setImageUri(uri: Uri?) {
        _uri.value = uri
    }

    fun emptyLiveData() {
        _photo.value = ""
        _source.value = ""
        _category.value = ""
        _listSteps.postValue(listOf())
        _listIngredients.postValue(listOf())
        _cook_time.postValue(0)
        _description.value = ""
        _hours.value = 0
        _last__id_steps = 0
        _last_id = 0
        _uri.value = null
        _minutes.value = 0
        _serves.value = ""
        _title.value = ""
    }

    fun setTabIndex(index: Int) {
        _chosenTabIndex.value = index
    }

    private var _listChosenMeals = MutableLiveData<List<Recipe>?>()

    val listChosenMeals: MutableLiveData<List<Recipe>?>
        get() = _listChosenMeals

    private var _listChosenMealsForSunday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForSunday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForSunday

    private var _listChosenMealsForMonday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForMonday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForMonday

    private var _listChosenMealsForTuesday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForTuesday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForTuesday

    private var _listChosenMealsForWednesday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForWednesday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForWednesday

    private var _listChosenMealsForThursday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForThursday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForThursday

    private var _listChosenMealsForFriday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForFriday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForFriday

    private var _listChosenMealsForSaturday = MutableLiveData<List<Recipe>?>()

    val listChosenMealsForSaturday: MutableLiveData<List<Recipe>?>
        get() = _listChosenMealsForSaturday

    fun performQueryForChosenMeals(
        dish: Recipe,
        dayId: Int,
    ) {
        if (dayId == 0) {
            if (_listChosenMealsForSunday.value?.contains(dish) == true) {
                _listChosenMealsForSunday.value =
                    _listChosenMealsForSunday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForSunday.value =
                    _listChosenMealsForSunday.value?.plus(dish) ?: listOf(dish)
            }
        } else if (dayId == 1) {
            if (_listChosenMealsForMonday.value?.contains(dish) == true) {
                _listChosenMealsForMonday.value =
                    _listChosenMealsForMonday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForMonday.value =
                    _listChosenMealsForMonday.value?.plus(dish) ?: listOf(dish)
            }
        } else if (dayId == 2) {
            if (_listChosenMealsForTuesday.value?.contains(dish) == true) {
                _listChosenMealsForTuesday.value =
                    _listChosenMealsForTuesday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForTuesday.value =
                    _listChosenMealsForTuesday.value?.plus(dish) ?: listOf(dish)
            }
        } else if (dayId == 3) {
            if (_listChosenMealsForWednesday.value?.contains(dish) == true) {
                _listChosenMealsForWednesday.value =
                    _listChosenMealsForWednesday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForWednesday.value =
                    _listChosenMealsForWednesday.value?.plus(dish) ?: listOf(dish)
            }
        } else if (dayId == 4) {
            if (_listChosenMealsForThursday.value?.contains(dish) == true) {
                _listChosenMealsForThursday.value =
                    _listChosenMealsForThursday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForThursday.value =
                    _listChosenMealsForThursday.value?.plus(dish) ?: listOf(dish)
            }
        } else if (dayId == 5) {
            if (_listChosenMealsForFriday.value?.contains(dish) == true) {
                _listChosenMealsForFriday.value =
                    _listChosenMealsForFriday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForFriday.value =
                    _listChosenMealsForFriday.value?.plus(dish) ?: listOf(dish)
            }
        } else if (dayId == 6) {
            if (_listChosenMealsForSaturday.value?.contains(dish) == true) {
                _listChosenMealsForSaturday.value =
                    _listChosenMealsForSaturday.value?.minus(dish) ?: listOf(dish)
            } else {
                _listChosenMealsForSaturday.value =
                    _listChosenMealsForSaturday.value?.plus(dish) ?: listOf(dish)
            }
        }
    }

    fun performQueryForChosenMealsFromDB(
        dayId: Int
    ) {
        if (dayId == 0) {
            _listChosenMealsForSunday.value = recipesForSunday.value
        } else if (dayId == 1) {
            _listChosenMealsForMonday.value = recipesForMonday.value
        } else if (dayId == 2) {
            _listChosenMealsForTuesday.value = recipesForTuesday.value
        } else if (dayId == 3) {
            _listChosenMealsForWednesday.value = recipesForWednesday.value
        } else if (dayId == 4) {
            _listChosenMealsForThursday.value = recipesForThursday.value
        } else if (dayId == 5) {
            _listChosenMealsForFriday.value = recipesForFriday.value
        } else if (dayId == 6) {
            _listChosenMealsForSaturday.value = recipesForSaturday.value
        }
    }

    fun getRecipe(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = recipeRepository.getRecipeById(id)
            returnedRecipe.postValue(recipe)
        }
    }

    fun getListOfIngredients(recipeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            returnedListIngredient.postValue(recipeRepository.getListOfIngredients(recipeId))
        }
    }

    fun getListOfSteps(recipeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            returnedListSteps.postValue(recipeRepository.getListOfSteps(recipeId))
        }
    }

    fun getCookTimeString(cookTime: Int?): String {
        if (cookTime == null) {
            return "0 min"
        } else {
            val hours: Int = cookTime!! / 60

            val minutes: Int = cookTime % 60
            return if (hours == 0) {
                "$minutes min"
            } else {
                "$hours h $minutes min"
            }
        }
    }

    fun setIsErrorTitle(isItError: Boolean) {
        _isErrorTitle.value = isItError
    }

    fun setIsValidUrl(isItValidUrl: Boolean) {
        _isValidUrl.value = isItValidUrl
    }

    fun getCookingComplexity(cookTime: Int?): String {
        return if (cookTime == null || cookTime <= 15) {
            "Easy"
        } else if (cookTime in 16..45) {
            "Medium"
        } else {
            "Hard"
        }
    }

    fun setChosenDay(dayId: Int) {
        _chosenDay.value = dayId
    }

    fun getAlfa(isMealPlanningOn: Boolean, recipe: Recipe): Float {
        if (isMealPlanningOn) {
            if (_chosenDay.value == 0) {
                if (_listChosenMealsForSunday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            } else if (_chosenDay.value == 1) {
                if (_listChosenMealsForMonday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            } else if (_chosenDay.value == 2) {
                if (_listChosenMealsForTuesday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            } else if (_chosenDay.value == 3) {
                if (_listChosenMealsForWednesday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            } else if (_chosenDay.value == 4) {
                if (_listChosenMealsForThursday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            } else if (_chosenDay.value == 5) {
                if (_listChosenMealsForFriday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            } else if (_chosenDay.value == 6) {
                if (_listChosenMealsForSaturday.value?.contains(recipe) == true) {
                    return 0.2F
                }
            }
        }
        return 1F
    }
}