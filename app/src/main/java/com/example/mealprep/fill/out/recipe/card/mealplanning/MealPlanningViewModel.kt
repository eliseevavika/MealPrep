package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.littlelemon.Dish

class MealPlanningViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Dish>?>()

    val list: MutableLiveData<List<Dish>?>
        get() = _list


    fun performQuery(
        dish: Dish
    ) {
        _list.value = _list.value?.plus(dish) ?: listOf(dish)
    }
}

