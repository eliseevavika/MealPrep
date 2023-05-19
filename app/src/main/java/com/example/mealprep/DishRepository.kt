package com.example.littlelemon

import androidx.annotation.DrawableRes
import com.example.mealprep.fill.out.recipe.card.Groceries
import com.example.meaprep.R


object DishRepository {
    val dishes = listOf(
        Dish(
            1,
            "Barley Salad with Feta and a Honey-Lemon dressing",
            "Barley Salad with Feta and a Honey-Lemon dressing",
            5,
            10,
            R.drawable.barleysalad,
            "https://victoriasidorova.wordpress.com/2018/12/26/barley-salad-with-feta-and-a-honey-lemon-dressing/",
            arrayListOf(
                Groceries(0, "1/2 cup uncooked pearl barley"),
                Groceries(1, "4 cups any washed green"),
                Groceries(2, "1/3 cup crumbled feta (at will)"),
                Groceries(3, "1 cup chickpeas"),
                Groceries(4, "1 avocado cubed (leave out until just before serving)"),
                Groceries(5, "2-3 tablespoons sunflower seeds"),
                Groceries(6, "2 tbsp red onion finely diced"),
                Groceries(7, "2 tbsp olive oil"),
                Groceries(8, "2 tbsp white wine vinegar (at will)"),
                Groceries(9, "1 tsp fresh lemon juice"),
                Groceries(10, "1/2 tsp lemon zest"),
                Groceries(11, "2 tsp honey")
            ),
            arrayListOf(
                "Cook pearl barley according to package directions",
                "Cook chickpeas with red pepper flakes, salt, black pepper, until they are golden.",
                "Mix the vinaigrette ingredients and red onion, so the onion will be marinated.",
                "Then mix herbs, barley, spicy cooked chickpeas, sunflower seeds, avocado and vinaigrette with onion",
                "At will, put feta on the top."
            )
        )
    )

    fun getDish(id: Int) = dishes.firstOrNull { it.id == id }
}

data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val prepTime: Int,
    val cookTimeTime: Int,
    @DrawableRes val imageResource: Int,
    val source: String,
    val ingredientsList: ArrayList<Groceries>,
    val instructions: ArrayList<String>
)