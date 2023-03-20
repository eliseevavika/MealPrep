package com.example.mealprep.fill.out.recipe.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.streams.asSequence


class ListIngredientsViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Groceries>>()
    private var _last_id: Int = -1

    val list: LiveData<List<Groceries>>
        get() = _list


//    init {
//        loadItems()
//    }

    //    fun loadItems() {
//        _list.postValue(ingredientsListData())
//    }


    fun performQuery(
        amount: String,
        ingredientName: String
    ): MutableLiveData<List<Groceries>> {
        val id = _last_id + 1
        _last_id = id
        val item = Groceries(id, amount, ingredientName)

        _list.value = _list.value?.plus(item) ?: listOf(item)

        return _list
    }

    fun removeElement(
        item: Groceries
    ): MutableLiveData<List<Groceries>> {
        _list.value = _list.value?.filter { it != item }
        return _list
    }
}

