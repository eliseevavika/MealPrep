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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class RecipeCreationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository

    var allRecipes: LiveData<List<Recipe>>

    val returnedRecipe = MutableLiveData<Recipe>()

    val returnedListIngredient = MutableLiveData<List<Ingredient>>()

    val returnedListSteps = MutableLiveData<List<Step>>()

    init {
        val recipeDao = AppDatabase.getDatabase(application).getRecipeDao()
        repository = RecipeRepository(recipeDao)
        allRecipes = repository.allRecipes
    }

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _hours = MutableStateFlow(0)
    val hours = _hours.asStateFlow()

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

    // List Ingredients
    private var _listIngredients = MutableLiveData<List<Groceries>>()
    private var _last_id: Int = -1

    val listIngredients: LiveData<List<Groceries>>
        get() = _listIngredients

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

    // List steps
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

    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(recipe)
    }

    // on below line we are creating a new method for updating a note. In this we are
    // calling a update method from our repository to update our note.
    fun updateRecipe(recipe: Recipe) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(recipe)
    }

    // on below line we are creating a new method for adding a new note to our database
    // we are calling a method from our repository to add a new note.
    fun addRecipe(recipe: Recipe) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRecipeIngredientAndStepTransaction(
                recipe,
                _listIngredients.value,
                _listSteps.value
            )
        }

    fun setImageUri(uri: Uri?) {
        _uri.value = uri
    }

    fun setTabIndex(index: Int) {
        _chosenTabIndex.value = index
    }

    private var _listChosenMeals = MutableLiveData<List<Recipe>?>()

    val listChosenMeals: MutableLiveData<List<Recipe>?>
        get() = _listChosenMeals

    fun performQueryForChosenMeals(
        dish: Recipe
    ) {
        if (_listChosenMeals.value?.contains(dish) == true) {
            _listChosenMeals.value = _listChosenMeals.value?.minus(dish) ?: listOf(dish)
        } else {
            _listChosenMeals.value = _listChosenMeals.value?.plus(dish) ?: listOf(dish)
        }
    }

    fun getRecipe(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            returnedRecipe.postValue(repository.getRecipeById(id))
        }
    }

    fun getListOfIngredients(recipeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            returnedListIngredient.postValue(repository.getListOfIngredients(recipeId))
        }
    }

    fun getListOfSteps(recipeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            returnedListSteps.postValue(repository.getListOfSteps(recipeId))
        }
    }
}