package com.example.mealprep.fill.out.recipe.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StepsCreationViewModel { private var _list = MutableLiveData<List<Steps>>()
    private var _last_id: Int = -1

    val list: LiveData<List<Steps>>
        get() = _list

    fun performQuery(
        ingredientName: String
    ) {
        val id = _last_id + 1
        _last_id = id
        val number = 1
        if (ingredientName.isNotEmpty()) {
            val item = Steps(id, number, ingredientName)

            _list.value = _list.value?.plus(item) ?: listOf(item)
        }
    }


    fun removeElement(
        item: Steps
    ) {
        _list.value = _list.value?.filter { it != item }
    }


    fun setName(
        item: Steps,
        input: String
    ) {
        val undatedItem = Steps(item.id, 1, input)

        _list.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.description = undatedItem.description
            }
        }

    }
}
