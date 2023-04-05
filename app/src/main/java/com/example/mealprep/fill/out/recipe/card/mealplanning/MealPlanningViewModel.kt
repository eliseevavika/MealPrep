package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.littlelemon.Dish
import com.example.littlelemon.DishRepository

class MealPlanningViewModel : ViewModel() {

    private var _list = MutableLiveData<List<Dish>?>()

    val list: MutableLiveData<List<Dish>?>
        get() = _list

    //Todo: later make chosenGroceries - type Groceries
    private var _chosenGroceries = MutableLiveData<List<String>?>()

    //Todo: later make chosenGroceries - type Groceries
    val chosenGroceries: MutableLiveData<List<String>?>
        get() = _chosenGroceries


    private var _listGroceries = MutableLiveData<List<String>?>()

    //Todo: later make listGroceries - type Groceries
    val listGroceries: MutableLiveData<List<String>?>
        get() = _listGroceries

    init {
        val dish = requireNotNull(DishRepository.getDish(1))

        val listIngredients = dish.ingredientsList
        _listGroceries.value = listIngredients

    }
    fun performQuery(
        dish: Dish
    ) {
        if (_list.value?.contains(dish) == true) {
            _list.value = _list.value?.minus(dish) ?: listOf(dish)
        } else {
            _list.value = _list.value?.plus(dish) ?: listOf(dish)
        }
    }


    fun performQueryForGroceries(
        item: String
    ) {
        if (_chosenGroceries.value?.contains(item) == true) {
            _chosenGroceries.value = _chosenGroceries.value?.minus(item) ?: listOf(item)
            _listGroceries.value = _listGroceries.value?.plus(item) ?: listOf(item)
        } else {
            _chosenGroceries.value = _chosenGroceries.value?.plus(item) ?: listOf(item)
            _listGroceries.value = _listGroceries.value?.minus(item) ?: listOf(item)
        }
    }

    fun addAllGroceries() {

    }


}

