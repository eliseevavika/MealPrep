package com.example.mealprep.fill.out.recipe.card.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mealprep.fill.out.recipe.card.Groceries


class ListIngredientsCreationViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Groceries>>()
    private var _last_id: Int = -1

    val list: LiveData<List<Groceries>>
        get() = _list

    fun performQuery(
        ingredientName: String
    ) {
        val id = _last_id + 1
        _last_id = id

        if (ingredientName.isNotEmpty()) {
            val item = Groceries(id, ingredientName)

            _list.value = _list.value?.plus(item) ?: listOf(item)
        }
    }

    fun removeElement(
        item: Groceries
    ) {
        _list.value = _list.value?.filter { it != item }
    }

    fun setName(
        item: Groceries,
        input: String
    ) {
        val undatedItem = Groceries(item.id, input)

        _list.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.name = undatedItem.name
            }
        }
    }
}