package com.example.mealprep.fill.out.recipe.card.groceries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.littlelemon.DishRepository
import com.example.mealprep.fill.out.recipe.card.Groceries

class GroceriesViewModel() : ViewModel() {
    private var _last_id: Int = -1

    private var _chosenGroceries = MutableLiveData<List<Groceries>?>()
    val chosenGroceries: MutableLiveData<List<Groceries>?>
        get() = _chosenGroceries

    private var _listGroceries = MutableLiveData<List<Groceries>?>()
    val listGroceries: MutableLiveData<List<Groceries>?>
        get() = _listGroceries

    private var _listExtras = MutableLiveData<List<Groceries>?>()
    val listExtras: MutableLiveData<List<Groceries>?>
        get() = _listExtras

    init {
        val dish = requireNotNull(DishRepository.getDish(1))
        val listIngredients = dish.ingredientsList
        _listGroceries.value = listIngredients
    }

    fun performQueryForGroceries(
        item: Groceries
    ) {
        if (_chosenGroceries.value?.contains(item) == true) {
            _chosenGroceries.value = _chosenGroceries.value?.minus(item) ?: listOf(item)
            _listGroceries.value = _listGroceries.value?.plus(item) ?: listOf(item)
        } else {
            _chosenGroceries.value = _chosenGroceries.value?.plus(item) ?: listOf(item)
            _listGroceries.value = _listGroceries.value?.minus(item) ?: listOf(item)
        }
    }

    fun performQuery(
        ingredientName: String
    ) {
        val id = _last_id + 1
        _last_id = id

        if (ingredientName.isNotEmpty()) {
            val item = Groceries(id, ingredientName)
            _listExtras.value = _listExtras.value?.plus(item) ?: listOf(item)
        }
    }

    fun removeElement(
        item: Groceries
    ) {
        _listExtras.value = _listExtras.value?.filter { it != item }
    }

    fun setName(
        item: Groceries,
        input: String
    ) {
        val undatedItem = Groceries(item.id, input)

        _listExtras.value?.forEach { grocery ->
            if (grocery.id == item.id) {
                grocery.name = undatedItem.name
            }
        }
    }

    fun addExtrasToTheList() {
        _listExtras.value?.forEach {
            _listGroceries.value = _listGroceries.value?.plus(it) ?: listOf(it)

        }
    }
}