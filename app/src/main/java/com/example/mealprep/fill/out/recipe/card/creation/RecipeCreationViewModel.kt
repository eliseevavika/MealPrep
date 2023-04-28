package com.example.mealprep.fill.out.recipe.card.creation

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.mealprep.AppDatabase
import com.example.mealprep.Recipe
import com.example.mealprep.fill.out.recipe.card.Groceries
import com.example.mealprep.fill.out.recipe.card.Steps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RecipeCreationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository
    private var allRecipes: LiveData<List<Recipe>>

    init {
        val recipeDao = AppDatabase.getDatabase(application).getRecipeDao()
        repository = RecipeRepository(recipeDao)
        allRecipes = repository.allRecipes
    }

    private var _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private var _hours = MutableLiveData<Int>()
    val hours: LiveData<Int>
        get() = _hours

    private var _minutes = MutableLiveData<Int>()
    val minutes: LiveData<Int>
        get() = _minutes

    private var _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private var _complexity = MutableLiveData<String>()
    val complexity: LiveData<String>
        get() = _complexity

    private var _photo = MutableLiveData<Bitmap>()
    val photo: LiveData<Bitmap>
        get() = _photo

    private var _cook_time = MutableLiveData<Float>()
    val cook_time: LiveData<Float>
        get() = _cook_time

    private var _serves = MutableLiveData<Int>()
    val serves: LiveData<Int>
        get() = _serves

    private var _source = MutableLiveData<String>()
    val source: LiveData<String>
        get() = _source

    private var _user_id = MutableLiveData<Int>()
    val user_id: LiveData<Int>
        get() = _user_id







    private var _categoryIndex = MutableLiveData<Int>()
    val categoryIndex: LiveData<Int>
        get() = _categoryIndex

    private var _servesIndex = MutableLiveData<Int>()
    val servesIndex: LiveData<Int>
        get() = _servesIndex




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

    fun setRecipeName(title: String?) {
        _title.value = title
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun isRquiredDataEntered() : Boolean {
        if (_title != null && !_listIngredients.value?.isEmpty()!! && !_listSteps.value?.isEmpty()!!) {
            return true
        }
        return false
    }

    fun addNewRecipe(){
        val recipe = Recipe(
            name = _title.value.toString(),
            description = _description.value,
            complexity = _complexity.value,
            photo = photo.value,
            cook_time = (_hours.value.toString() +"."+ _minutes.value.toString()).toFloat(),
            serves = _serves.value,
            source = _source.value,
            user_id = _user_id.value!!.toInt(),
            category_id = _categoryIndex.value,
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
    fun addRecipe(recipe: Recipe) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(recipe)

    }
}
