package com.sliceup.mealprep.viewmodel

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.sliceup.mealprep.data.RecipeRepository
import com.sliceup.mealprep.ui.mealplanning.MealPlanWithLinks
import com.sliceup.mealprep.ui.mealplanning.RecipeWithLink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.sliceup.mealprep.data.AppDatabase
import com.sliceup.mealprep.data.Ingredient
import com.sliceup.mealprep.data.Recipe
import com.sliceup.mealprep.data.RecipeWithMealPlan
import com.sliceup.mealprep.data.Step
import com.sliceup.mealprep.data.model.Aisle
import com.sliceup.mealprep.data.model.IngredientAisleInfo
import com.sliceup.mealprep.data.model.days
import com.sliceup.mealprep.data.model.sortedFamiliarIngredients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeRepository: RecipeRepository

    var allRecipes: LiveData<List<Recipe>>

    var recipesForSunday: Flow<List<Recipe>>
    var recipesForMonday: Flow<List<Recipe>>
    var recipesForTuesday: Flow<List<Recipe>>
    var recipesForWednesday: Flow<List<Recipe>>
    var recipesForThursday: Flow<List<Recipe>>
    var recipesForFriday: Flow<List<Recipe>>
    var recipesForSaturday: Flow<List<Recipe>>

    val returnedRecipe = MutableLiveData<Recipe>()

    val recipeNameForTooltip = MutableLiveData<String>()

    val returnedListIngredient = MutableLiveData<List<Ingredient>>()

    val returnedListSteps = MutableLiveData<List<Step>>()

    var ingredientsFromMealPlans: LiveData<List<Pair<Ingredient, Int>>>

    var listGroceriesForAnotherStore: LiveData<List<Ingredient>>

    var completedIngredients: LiveData<List<Ingredient>>

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

        ingredientsFromMealPlans = recipeRepository.ingredientsFromMealPlans
        listGroceriesForAnotherStore = recipeRepository.listGroceriesForAnotherStore
        completedIngredients = recipeRepository.completedIngredients
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

    private val _newAisleChoice = MutableStateFlow(-1)
    val newAisleChoice = _newAisleChoice.asStateFlow()

    private val _chosenTabIndex = MutableStateFlow(0)
    val chosenTabIndex = _chosenTabIndex.asStateFlow()

    private var _listIngredients = MutableLiveData<List<Ingredient>>()

    val listIngredients: LiveData<List<Ingredient>>
        get() = _listIngredients

    private val _isErrorTitle = MutableStateFlow(false)
    val isErrorTitle = _isErrorTitle.asStateFlow()

    private val _isValidUrl = MutableStateFlow(true)
    val isValidUrl = _isValidUrl.asStateFlow()

    private val _expandMainStore = MutableStateFlow(true)
    val expandMainStore = _expandMainStore.asStateFlow()


    private var _listExtraGroceries = MutableLiveData<List<Ingredient>?>()
    val listExtraGroceries: MutableLiveData<List<Ingredient>?>
        get() = _listExtraGroceries

    private val _ingredientsInput = MutableStateFlow("")
    val ingredientsInput = _ingredientsInput.asStateFlow()

    fun refreshDataHomeForCurrentUser() {
        recipeRepository.refreshDataForHome()
        allRecipes = recipeRepository.allRecipes
    }

    fun refreshDataMealPrepForCurrentUser() {
        recipeRepository.refreshDataForMealPrep()
        recipesForSunday = recipeRepository.recipesForSunday
        recipesForMonday = recipeRepository.recipesForMonday
        recipesForTuesday = recipeRepository.recipesForTuesday
        recipesForWednesday = recipeRepository.recipesForWednesday
        recipesForThursday = recipeRepository.recipesForThursday
        recipesForFriday = recipeRepository.recipesForFriday
        recipesForSaturday = recipeRepository.recipesForSaturday
    }

    fun refreshDataGroceriesForCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.refreshDataForGroceries()
            ingredientsFromMealPlans = recipeRepository.ingredientsFromMealPlans
            listGroceriesForAnotherStore = recipeRepository.listGroceriesForAnotherStore
            completedIngredients = recipeRepository.completedIngredients
        }
    }

    fun performQueryIngredients(
        ingredientName: String
    ) {
        if (ingredientName.isNotEmpty()) {
            val list = parseStringByNewLine(ingredientName)
            if (list.isNotEmpty()) {
                list.forEach { ingredientName ->
                    val ingredient = Ingredient(
                        name = ingredientName,
                        completed = false,
                        completion_date = null,
                        recipe_id = 0,
                        aisle = 0,
                        short_name = "",
                        user_uid = ""
                    )
                    _listIngredients.value =
                        _listIngredients.value?.plus(ingredient) ?: listOf(ingredient)
                }
            }
        }
    }

    fun setIngredientsInpitValueAsEmptyString() {
        _ingredientsInput.value = ""
    }

    fun parseStringByNewLine(input: String): List<String> {
        return input.split("\n").map { it.trim() }
    }

    fun removeElementIngredients(
        item: Ingredient
    ) {
        _listIngredients.value = _listIngredients.value?.filter { it != item }
    }

    fun setNameIngredients(
        item: Ingredient, input: String
    ) {
        _listIngredients.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.name = input
            }
        }
    }

    fun  importDataFromFile(
        inputStream: InputStream?,
        showSuccessMessage: () -> Unit,
        showError: (String) -> Unit
    ) {
        inputStream?.let {
            try {
                val jsonContent = it.bufferedReader().use { reader -> reader.readText() }
                val array = JsonParser.parseString(jsonContent).asJsonArray
                val gson = Gson()
                val typeRecipe = object : TypeToken<List<Recipe>>() {}.type
                val typeIngredient = object : TypeToken<List<Ingredient>>() {}.type
                val typeRecipeWithMealPlan = object : TypeToken<List<RecipeWithMealPlan>>() {}.type
                val typeStep = object : TypeToken<List<Step>>() {}.type

                val allRecipes: List<Recipe> = gson.fromJson(array[0], typeRecipe)
                val allIngredients: List<Ingredient> = gson.fromJson(array[1], typeIngredient)
                val allRecipesWithMealPlan: List<RecipeWithMealPlan> =
                    gson.fromJson(array[2], typeRecipeWithMealPlan)
                val allSteps: List<Step> = gson.fromJson(array[3], typeStep)

                allRecipes.forEach { recipe ->
                    recipe.user_uid = getUserUid()
                }

                allIngredients.forEach { ingredient ->
                    ingredient.user_uid = getUserUid()
                }
                saveDataToDatabase(allRecipes, allIngredients, allRecipesWithMealPlan, allSteps)
                showSuccessMessage()
            } catch (e: IOException) {
                showError("Error, file cannot be uploaded")
            }
        }
    }

    fun saveDataToDatabase(
        allRecipes: List<Recipe>,
        allIngredients: List<Ingredient>,
        allRecipesWithMealPlan: List<RecipeWithMealPlan>,
        allSteps: List<Step>
    ) {
        viewModelScope.launch {
            recipeRepository.clearDatabase()
            if (allRecipes.isNotEmpty()) {
                recipeRepository.insertAllRecipes(allRecipes)
            }
            if (allIngredients.isNotEmpty()) {
                recipeRepository.insertAllIngredients(allIngredients)
            }
            if (allRecipesWithMealPlan.isNotEmpty()) {
                recipeRepository.insertAllRecipeWithMealplanData(allRecipesWithMealPlan)
            }
            if (allSteps.isNotEmpty()) {
                recipeRepository.insertAllSteps(allSteps)
            }
        }
    }

    fun import(file: File): String? {
        return try {
            val jsonContent = file.readText()
            jsonContent
        } catch (e: IOException) {
            null
        }
    }

    suspend fun export(): String? {
        return withContext(Dispatchers.IO) {
            val recipes: List<Recipe> = allRecipes.value.orEmpty()

            val ingredients: List<Ingredient> = recipeRepository.getAllIngredients(getUserUid())

            val recipesWithMealPlans: List<RecipeWithMealPlan> =
                recipeRepository.getAllRecipesWithMealPlans()

            val steps: List<Step> = recipeRepository.getAllSteps()

            val allDataLists = listOf(
                recipes, ingredients, recipesWithMealPlans, steps
            )
            val gson = Gson()
            val json = gson.toJson(allDataLists)

            json
        }
    }

    private var _listSteps = MutableLiveData<List<Step>>()
    val listSteps: LiveData<List<Step>>
        get() = _listSteps

    fun performQuerySteps(
        stepName: String
    ) {
        if (stepName.isNotEmpty()) {
            val list = parseStringByNewLine(stepName)
            if (list.isNotEmpty()) {
                list.forEach { stepDescription ->
                    val step = Step(
                        description = stepDescription, recipe_id = 0
                    )
                    _listSteps.value = _listSteps.value?.plus(step) ?: listOf(step)
                }
            }
        }
    }

    fun performQueryForExtraGroceries(
        ingredientName: String
    ) {
        val currentUserUID: String = Firebase.auth.currentUser?.uid.toString()

        if (ingredientName.isNotEmpty()) {
            val ingredientAisleInfo = findAisleForGrocery(ingredientName)
            val aisleNumber = ingredientAisleInfo.aisle.value
            val ingredientShortName = ingredientAisleInfo.shortName

            val ingredient = Ingredient(
                name = ingredientName,
                completed = false,
                completion_date = null,
                recipe_id = null,
                aisle = aisleNumber,
                short_name = ingredientShortName,
                user_uid = currentUserUID
            )
            _listExtraGroceries.value =
                _listExtraGroceries.value?.plus(ingredient) ?: listOf(ingredient)
        }
    }

    fun setNameForExtraGrocery(
        item: Ingredient, input: String
    ) {
        _listExtraGroceries.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.name = input
            }
        }
    }

    fun removeElementFromListExtraFroceries(
        item: Ingredient
    ) {
        _listExtraGroceries.value = _listExtraGroceries.value?.filter { it != item }
    }

    fun addExtraGroceriesToTheDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _listExtraGroceries.value?.forEach { ingredient ->
                recipeRepository.insertExtraIngredientToDB(ingredient)
            }
            emptyLiveDataForExtraGroceries()
        }
    }

    fun removeElementSteps(
        item: Step
    ) {
        _listSteps.value = _listSteps.value?.filter { it != item }
    }

    fun setNameSteps(
        item: Step, input: String
    ) {
        _listSteps.value?.forEach { step ->
            if (step.id == item.id) {
                step.description = input
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

    fun setNewAsleChoice(count: Int) {
        if (count != -1) {
            _newAisleChoice.value = count
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
            user_uid = getUserUid(),
            category = _category.value,
            creation_date = Calendar.getInstance().time
        )

        addRecipe(recipe, updateImageFromFirebase = { recipeId ->
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("gs://slice up-bbbb7.appspot.com")
            val imageFileUri: Uri = _photo.value.toUri()

            if (imageFileUri.pathSegments.size != 0) {
                val fileName = imageFileUri.pathSegments.last() + recipeId

                storageRef.child(fileName).putFile(imageFileUri)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.task.result.storage.downloadUrl.addOnSuccessListener { uri ->
                            viewModelScope.launch(Dispatchers.IO) {
                                recipeRepository.updateRecipeImageFromFirebase(recipeId, uri)
                            }
                        }
                    }.addOnFailureListener { exception ->
                        (exception.message.toString())
                    }
            }
        }, updateAisleForAllGroceries = { recipeId ->
            viewModelScope.launch(Dispatchers.IO) {
                val listOfIngredients = recipeRepository.getListOfIngredients(recipeId)
                listOfIngredients.forEach { ingredient ->
                    val name = ingredient.name

                    val ingredientAisleInfo = findAisleForGrocery(name)
                    val aisleNumber = ingredientAisleInfo.aisle.value
                    val ingredientShortName = ingredientAisleInfo.shortName
                    recipeRepository.updateAisleForAllGroceries(
                        ingredient.id, aisleNumber, ingredientShortName
                    )
                }
            }
        })
    }

    fun editExistingRecipe(recipeId: Long) {
        val recipeWithUpdatedFields = Recipe(
            name = _title.value,
            description = _description.value,
            photo = _photo.value,
            cook_time = _cook_time.value,
            serves = if (_serves.value == "") 0 else _serves.value.toInt(),
            source = _source.value,
            user_uid = getUserUid(),
            category = _category.value,
            creation_date = Calendar.getInstance().time
        )

        editRecipe(recipeId, recipeWithUpdatedFields, updateImageFromFirebase = { recipeId ->
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("gs://slice up-bbbb7.appspot.com")
            val imageFileUri: Uri = _photo.value.toUri()

            if (imageFileUri.pathSegments.size != 0) {
                val fileName = imageFileUri.pathSegments.last()

                storageRef.child(fileName).putFile(imageFileUri)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.task.result.storage.downloadUrl.addOnSuccessListener { uri ->
                            viewModelScope.launch(Dispatchers.IO) {
                                recipeRepository.updateRecipeImageFromFirebase(recipeId, uri)
                            }
                        }
                    }.addOnFailureListener { exception ->
                        (exception.message.toString())
                    }
            }
        }, updateAisleForAllGroceries = { recipeId ->
            viewModelScope.launch(Dispatchers.IO) {
                val listOfIngredients = recipeRepository.getListOfIngredients(recipeId)
                listOfIngredients.forEach { ingredient ->
                    val name = ingredient.name

                    val ingredientAisleInfo = findAisleForGrocery(name)
                    val aisleNumber = ingredientAisleInfo.aisle.value
                    val ingredientShortName = ingredientAisleInfo.shortName
                    recipeRepository.updateAisleForAllGroceries(
                        ingredient.id, aisleNumber, ingredientShortName
                    )
                }
            }
        })
    }

    private fun editRecipe(
        recipeId: Long,
        recipeWithUpdatedFields: Recipe,
        updateImageFromFirebase: (Long) -> Unit,
        updateAisleForAllGroceries: (Long) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.updateRecipeWithNewData(
                recipeId,
                recipeWithUpdatedFields,
                _listIngredients.value,
                _listSteps.value,
                getUserUid()
            )
            updateImageFromFirebase(recipeId)
            updateAisleForAllGroceries(recipeId)
            emptyLiveData()
        }
    }

    fun getUserUid(): String {
        val user = Firebase.auth.currentUser
        return user?.uid ?: "0"
    }

    fun getUserEmail(): String? {
        return Firebase.auth.currentUser?.email
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

    fun addRecipe(
        recipe: Recipe,
        updateImageFromFirebase: (Long) -> Unit,
        updateAisleForAllGroceries: (Long) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeId = recipeRepository.insertRecipeIngredientAndStepTransaction(
                recipe, _listIngredients.value, _listSteps.value, getUserUid()
            )
            updateImageFromFirebase(recipeId)
            updateAisleForAllGroceries(recipeId)
            emptyLiveData()
        }
    }

    fun addMealPlan(dayId: Int) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepository.deleteRecipeAndMealPlanTransaction(dayId)
        recipeRepository.insertRecipeAndMealPlanTransaction(dayId, _listChosenMeals.value)
    }

    fun setImageUri(uri: Uri?) {
        _uri.value = uri
    }

    fun performQueryForGroceries(
        ingredient: Ingredient
    ) {
        if (completedIngredients.value?.contains(ingredient) == true) {
            makeIngredientActing(ingredient)
        } else {
            makeIngredientComplete(ingredient)
        }
    }

    private fun makeIngredientActing(ingredient: Ingredient) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.makeIngredientActing(ingredient)
        }
    }

    fun makeIngredientComplete(ingredient: Ingredient) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.makeIngredientComplete(ingredient)
        }
    }

    fun emptyLiveData() {
        _photo.value = ""
        _source.value = ""
        _isValidUrl.value = true
        _category.value = ""
        _listSteps.postValue(listOf())
        _listIngredients.postValue(listOf())
        _cook_time.postValue(0)
        _description.value = ""
        _hours.value = 0
        _uri.value = null
        _minutes.value = 0
        _serves.value = ""
        _title.value = ""
    }

    fun emptyLiveDataForExtraGroceries() {
        _listExtraGroceries.postValue(emptyList())
    }

    fun setTabIndex(index: Int) {
        _chosenTabIndex.value = index
    }

    private var _listChosenMeals = MutableLiveData<List<Recipe>?>()
    val listChosenMeals: MutableLiveData<List<Recipe>?>
        get() = _listChosenMeals


    private var _listChosenMealsForSunday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForSunday: StateFlow<List<Recipe>>
        get() = _listChosenMealsForSunday

    private var _listChosenMealsForMonday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForMonday: MutableStateFlow<List<Recipe>>
        get() = _listChosenMealsForMonday

    private var _listChosenMealsForTuesday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForTuesday: MutableStateFlow<List<Recipe>>
        get() = _listChosenMealsForTuesday

    private var _listChosenMealsForWednesday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForWednesday: MutableStateFlow<List<Recipe>>
        get() = _listChosenMealsForWednesday

    private var _listChosenMealsForThursday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForThursday: MutableStateFlow<List<Recipe>>
        get() = _listChosenMealsForThursday

    private var _listChosenMealsForFriday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForFriday: MutableStateFlow<List<Recipe>>
        get() = _listChosenMealsForFriday

    private var _listChosenMealsForSaturday = MutableStateFlow<List<Recipe>>(emptyList())

    val listChosenMealsForSaturday: MutableStateFlow<List<Recipe>>
        get() = _listChosenMealsForSaturday

    fun performQueryForChosenMeals(
        dish: Recipe,
        dayId: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (dayId == 0) {
                if (_listChosenMealsForSunday.value.contains(dish)) {
                    _listChosenMealsForSunday.value =
                        _listChosenMealsForSunday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForSunday.value =
                        _listChosenMealsForSunday.value.plus(dish) ?: listOf(dish)
                }
            } else if (dayId == 1) {
                if (_listChosenMealsForMonday.value.contains(dish)) {
                    _listChosenMealsForMonday.value =
                        _listChosenMealsForMonday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForMonday.value =
                        _listChosenMealsForMonday.value.plus(dish) ?: listOf(dish)
                }
            } else if (dayId == 2) {
                if (_listChosenMealsForTuesday.value.contains(dish)) {
                    _listChosenMealsForTuesday.value =
                        _listChosenMealsForTuesday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForTuesday.value =
                        _listChosenMealsForTuesday.value.plus(dish) ?: listOf(dish)
                }
            } else if (dayId == 3) {
                if (_listChosenMealsForWednesday.value.contains(dish)) {
                    _listChosenMealsForWednesday.value =
                        _listChosenMealsForWednesday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForWednesday.value =
                        _listChosenMealsForWednesday.value.plus(dish) ?: listOf(dish)
                }
            } else if (dayId == 4) {
                if (_listChosenMealsForThursday.value.contains(dish)) {
                    _listChosenMealsForThursday.value =
                        _listChosenMealsForThursday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForThursday.value =
                        _listChosenMealsForThursday.value.plus(dish) ?: listOf(dish)
                }
            } else if (dayId == 5) {
                if (_listChosenMealsForFriday.value.contains(dish)) {
                    _listChosenMealsForFriday.value =
                        _listChosenMealsForFriday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForFriday.value =
                        _listChosenMealsForFriday.value.plus(dish) ?: listOf(dish)
                }
            } else if (dayId == 6) {
                if (_listChosenMealsForSaturday.value.contains(dish)) {
                    _listChosenMealsForSaturday.value =
                        _listChosenMealsForSaturday.value.minus(dish) ?: listOf(dish)
                } else {
                    _listChosenMealsForSaturday.value =
                        _listChosenMealsForSaturday.value.plus(dish) ?: listOf(dish)
                }
            }
        }
    }

    fun performQueryForChosenMealsFromDB(
        dayId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (dayId) {
                0 -> {
                    recipesForSunday.collect { recipes ->
                        _listChosenMealsForSunday.value = recipes
                    }
                }

                1 -> {
                    recipesForMonday.collect { recipes ->
                        _listChosenMealsForMonday.value = recipes
                    }
                }

                2 -> {
                    recipesForTuesday.collect { recipes ->
                        _listChosenMealsForTuesday.value = recipes
                    }
                }

                3 -> {
                    recipesForWednesday.collect { recipes ->
                        _listChosenMealsForWednesday.value = recipes
                    }
                }

                4 -> {
                    recipesForThursday.collect { recipes ->
                        _listChosenMealsForThursday.value = recipes
                    }
                }

                5 -> {
                    recipesForFriday.collect { recipes ->
                        _listChosenMealsForFriday.value = recipes
                    }
                }

                6 -> {
                    recipesForSaturday.collect { recipes ->
                        _listChosenMealsForSaturday.value = recipes
                    }
                }
            }
        }
    }

    fun getRecipe(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = recipeRepository.getRecipeById(id)
            returnedRecipe.postValue(recipe)
        }
    }

    fun getTextForTooltipBox(recipeId: Long?) {
        if (recipeId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val recipe = recipeRepository.getRecipeById(recipeId)
                recipeNameForTooltip.postValue(recipe.name)
            }
        } else {
            recipeNameForTooltip.postValue(null)
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
                "${hours}h ${minutes}min"
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

    fun deleteAllRecipesForDay(dayId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.makeAllIgredientsActive(dayId)
            recipeRepository.deleteRecipeAndMealPlanTransaction(dayId)
        }
    }

    fun findAisleForGrocery(input: String): IngredientAisleInfo {
        val words = input.split("\\s".toRegex())
        val result = mutableListOf<String>()

        for (word in words) {
            val lowercaseWord = word.lowercase()
            val commaParts = lowercaseWord.split(",".toRegex())
            result.addAll(commaParts)

            result.forEach { part ->
                for ((ingredient, aisle) in sortedFamiliarIngredients) {
                    val similarity = 1.0 - StringUtils.getLevenshteinDistance(ingredient, part)
                        .toDouble() / Math.max(ingredient.length, part.length)

                    if (similarity >= 0.83) {
                        return IngredientAisleInfo(aisle, ingredient)
                    }
                }
            }
            result.clear()
        }
        return IngredientAisleInfo(Aisle.OTHERS, input)
    }

    fun updateAisleNumber(ingredient: Ingredient, aisleNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.updateAisleNumber(ingredient.id, aisleNumber)
        }
    }

    fun setExpandMainStore(expandMainStoreParam: Boolean) {
        _expandMainStore.value = expandMainStoreParam
    }

    fun markAllComplete() {
        viewModelScope.launch(Dispatchers.IO) {
            ingredientsFromMealPlans = recipeRepository.ingredientsFromMealPlans
            ingredientsFromMealPlans.value?.forEach { ingredient ->
                makeIngredientComplete(ingredient.first)
            }
            listGroceriesForAnotherStore = recipeRepository.listGroceriesForAnotherStore

            listGroceriesForAnotherStore.value?.forEach { ingredient ->
                makeIngredientComplete(ingredient)
            }
        }
    }

    fun makeCopyMealPlansWithLinks(
        recipesForSunday: List<Recipe>,
        recipesForMonday: List<Recipe>,
        recipesForTuesday: List<Recipe>,
        recipesForWednesday: List<Recipe>,
        recipesForThursday: List<Recipe>,
        recipesForFriday: List<Recipe>,
        recipesForSaturday: List<Recipe>
    ): List<MealPlanWithLinks> {
        val resultList = mutableListOf<MealPlanWithLinks>()

        if (recipesForSunday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(0, recipesForSunday))
        }
        if (recipesForMonday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(1, recipesForMonday))
        }

        if (recipesForTuesday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(2, recipesForTuesday))
        }

        if (recipesForWednesday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(3, recipesForWednesday))
        }

        if (recipesForThursday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(4, recipesForThursday))
        }

        if (recipesForFriday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(5, recipesForFriday))
        }

        if (recipesForSaturday.isNotEmpty()) {
            resultList.add(getMealPlanRecipesWithLinks(6, recipesForSaturday))
        }
        return resultList
    }

    fun getMealPlanRecipesWithLinks(day: Int, recipesByDay: List<Recipe>): MealPlanWithLinks {
        val list = mutableListOf<RecipeWithLink>()

        recipesByDay.forEach { recipe ->
            val recipeName = recipe.name
            val recipeLink = recipe.source
            list.add(RecipeWithLink(recipeName, recipeLink))
        }
        return MealPlanWithLinks(day, list)
    }

    fun makeJoinedNameForSpecificMealPlan(mealPlan: MealPlanWithLinks): List<String> {
        val day = mealPlan.dayOfWeek
        val result = mutableListOf<String>()
        result.add(days[day].title)
        result.add("")

        mealPlan.recipesWithLinks.forEach { recipe ->
            val name = recipe.recipeName.uppercase()
            val link = recipe.recipeLink
            result.add(name)
            link?.let { result.add(it) }
            result.add("")
        }
        return result.toList()
    }

    fun resetAllMealPlansForTheWeek() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllRecipesForDay(0)
            deleteAllRecipesForDay(1)
            deleteAllRecipesForDay(2)
            deleteAllRecipesForDay(3)
            deleteAllRecipesForDay(4)
            deleteAllRecipesForDay(5)
            deleteAllRecipesForDay(6)
        }
    }

    fun checkIfSomethingFilled(): Boolean {
        return (_title.value != "" || _hours.value != 0
                || _minutes.value != 0 || _description.value != ""
                || _category.value != "" || _serves.value != ""
                || _source.value != "" || _uri.value != null
                || _listIngredients.value != null
                || _listSteps.value != null)
    }

    fun getHours(cookTime: Int?): Int {
        return if (cookTime != null) {
            cookTime / 60
        } else {
            0
        }
    }


    fun getMinuts(cookTime: Int?): Int {
        return if (cookTime != null) {
            cookTime % 60
        } else {
            0
        }
    }

    fun getAllStateDataForRecipe(recipe: Recipe) {
        emptyLiveData()
        _title.value = recipe.name
        _description.value = recipe.description ?: ""
        _hours.value = getHours(recipe.cook_time ?: 0)
        _minutes.value = getMinuts(recipe.cook_time ?: 0)
        _serves.value = recipe.serves?.toString() ?: "0"
        _source.value = recipe.source ?: ""
        _category.value = recipe.category ?: ""
        _photo.value = recipe.photo ?: ""
        _uri.value = if (_photo.value == "") {
            null
        } else {
            _photo.value.toUri()
        }

        viewModelScope.launch(Dispatchers.IO) {
            _listIngredients.postValue(recipeRepository.getListOfIngredients(recipe.recipe_id))
            _listSteps.postValue(recipeRepository.getListOfSteps(recipe.recipe_id))
        }
    }

    fun deleteTheRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.deleteTheRecipe(
                recipe,
                getUserUid()
            )
            deleteImageFromFirebase(recipe)
        }
    }

    private fun deleteImageFromFirebase(recipe: Recipe) {
        val storage = Firebase.storage
        val imagePath = recipe.photo

        if (!imagePath.isNullOrEmpty()) {
            val uri = Uri.parse(imagePath)
            val path = uri.path
            val fileName = uri.pathSegments.last()
            if (path != null) {
                val storageRef = storage.reference.child(fileName)
                storageRef.delete()
                    .addOnSuccessListener {
                        Log.e("FirebasePhotoDelete", "Success deleting image from Firebase Storage")
                    }
                    .addOnFailureListener {
                        Log.e(
                            "FirebasePhotoDelete",
                            "Error deleting image from Firebase Storage",
                            it
                        )
                    }
            }
        }
    }

    fun setIngredientsInput(input: String) {
        _ingredientsInput.value = input
    }

    fun signAsAGuest(onSuccess: (String) -> Unit, onError: (String?) -> Unit) {
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val uid = currentUser?.uid
                    if (uid != null) {
                        onSuccess(uid)
                    } else {
                        onError("Failed to get UID for anonymous user")
                    }
                } else {
                    onError(task.exception?.message)
                }
            }
    }
}