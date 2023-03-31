package com.example.mealprep.fill.out.recipe.card.mealplanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.littlelemon.Dish
import com.example.mealprep.fill.out.recipe.card.Groceries

class MealPlanningViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Dish>>()
//    private var _last_id: Int = -1

    val list: LiveData<List<Dish>>
        get() = _list

    fun performQuery(
        dish: Dish
    ) {
        _list.value = _list.value?.plus(dish) ?: listOf(dish)
    }


//    fun removeElement(
//        item: Groceries
//    ) {
//        _list.value = _list.value?.filter { it != item }
//    }
//
//
//    fun setName(
//        item: Groceries,
//        input: String
//    ) {
//        val undatedItem = Groceries(item.id, input)
//
//        _list.value?.forEach { grocery ->
//            if (grocery.id == item.id) {
//                grocery.name = undatedItem.name
//            }
//        }
//
//    }
}

