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
        if(_list.value?.contains(dish) == true){
            _list.value = _list.value?.minus(dish) ?: listOf(dish)
        }else{
            _list.value = _list.value?.plus(dish) ?: listOf(dish)
        }
    }


}

