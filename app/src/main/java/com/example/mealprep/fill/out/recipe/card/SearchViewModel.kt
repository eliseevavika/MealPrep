package com.example.mealprep.fill.out.recipe.card

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SearchViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Groceries>>()

    val list: LiveData<List<Groceries>>
        get() = _list


//    init {
//        loadItems()
//    }

    //    fun loadItems() {
//        _list.postValue(ingredientsListData())
//    }
    fun addRecord(amount: String, ingredientName: String) {
//        todoList.add(Groceries(amount, ingredientName))
    }

    fun performQuery(
        amount: String,
        ingredientName: String
    ): MutableLiveData<List<Groceries>> {
        val item = Groceries(amount, ingredientName)

        _list.value = _list.value?.plus(item) ?: listOf(item)

        return _list
    }

//    private fun ingredientsListData(): List<Groceries> {
//        val data : List<Pair<Int, String>> = listOf(Pair(0, "milk"), Pair(1, "bread"), Pair(2, "potato"), Pair(3, "tomatoes"),Pair(4, "onion"), Pair(5, "red onion"), Pair(6, "carrot"), Pair(7, "strawberries"),Pair(8, "cheese"),Pair(9, "avocado"))
//        val groceriesList = ArrayList<Groceries>()
//        data.forEach {
//            groceriesList.add(Groceries(it.first, it.second))
//        }
//
//        return groceriesList
//    }
}

