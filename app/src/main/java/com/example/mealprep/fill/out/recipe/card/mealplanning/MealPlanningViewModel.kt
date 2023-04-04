package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.littlelemon.Dish
import com.example.mealprep.fill.out.recipe.card.Groceries

class MealPlanningViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Dish>?>()

    val list: MutableLiveData<List<Dish>?>
        get() = _list

    //Todo: later make chosenGroceries - type Groceries
    private var _chosenGroceries = MutableLiveData<List<String>?>()

    //Todo: later make chosenGroceries - type Groceries
    val chosenGroceries: MutableLiveData<List<String>?>
        get() = _chosenGroceries


    fun performQuery(
        dish: Dish
    ) {
        if(_list.value?.contains(dish) == true){
            _list.value = _list.value?.minus(dish) ?: listOf(dish)
        }else{
            _list.value = _list.value?.plus(dish) ?: listOf(dish)
        }
    }

    fun performQueryForGroceries(
        item: String
    ) {
        if(_chosenGroceries.value?.contains(item) == true){
            _chosenGroceries.value = _chosenGroceries.value?.minus(item) ?: listOf(item)
        }else{
            _chosenGroceries.value = _chosenGroceries.value?.plus(item) ?: listOf(item)
        }
    }


}

