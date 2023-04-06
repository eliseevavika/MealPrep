package com.example.littlelemon

import androidx.annotation.DrawableRes
import com.example.mealprep.fill.out.recipe.card.Groceries
import com.example.meaprep.R
import java.util.logging.Level


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
                Groceries(3,  "1 cup chickpeas"),
                Groceries(4,  "1 avocado cubed (leave out until just before serving)"),
                Groceries(5,  "2-3 tablespoons sunflower seeds"),
                Groceries(6, "2 tbsp red onion finely diced"),
                Groceries(7, "2 tbsp olive oil"),
                Groceries(8, "2 tbsp white wine vinegar (at will)"),
                Groceries(9, "1 tsp fresh lemon juice"),
                Groceries(10, "1/2 tsp lemon zest"),
                Groceries(11,  "2 tsp honey")
            ),
            arrayListOf(
                "Cook pearl barley according to package directions",
                "Cook chickpeas with red pepper flakes, salt, black pepper, until they are golden.",
                "Mix the vinaigrette ingredients and red onion, so the onion will be marinated.",
                "Then mix herbs, barley, spicy cooked chickpeas, sunflower seeds, avocado and vinaigrette with onion",
                "At will, put feta on the top."
            )
        ),
        Dish(
            2,
            "The Meatball Hot Dog",
            "The Meatball Hot Dog",
            10,
            15,
            R.drawable.hotdog,
            "https://victoriasidorova.wordpress.com/2019/01/03/the-meatball-hot-dog/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            3,
            "Indian lentil soup Masoor...",
            "Indian lentil soup Masoor...",
            10,
            30,
            R.drawable.masoordal,
            "https://victoriasidorova.wordpress.com/2018/12/31/indian-lentil-soup-masoor-dal/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            4,
            "Chicken Cacciatore",
            "Chicken Cacciatore",
            15,
            60,
            R.drawable.cacciatore,
            "https://victoriasidorova.wordpress.com/2019/02/17/chicken-cacciatore/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            5,
            "Beetroot and orange salad",
            "Beetroot and orange salad",
            15,
            25,
            R.drawable.beetrootsalad,
            "https://victoriasidorova.wordpress.com/2019/02/21/beetroot-and-orange-salad/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            6,
            "Frozen Yogurt Granola Cups",
            "Frozen Yogurt Granola Cups",
            15,
            120,
            R.drawable.frozenyougurt,
            "https://victoriasidorova.wordpress.com/2019/04/11/frozen-yogurt-granola-cups/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            7,
            "Pasta farfalle or spirals with ...",
            "Pasta farfalle or spirals with ...",
            15,
            35,
            R.drawable.farfallepasta,
            "https://victoriasidorova.wordpress.com/2019/09/04/pasta-farfalle-or-spirals-with-vegetables/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            8,
            "Mini Caprese and Spanish...",
            "Mini Caprese and Spanish",
            10,
            30,
            R.drawable.minicaprese,
            "https://victoriasidorova.wordpress.com/2019/04/25/mini-caprese-and-spanish-frittatas/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            9,
            "Lentil soup with salmon and...",
            "Lentil soup with salmon and... .",
            15,
            45,
            R.drawable.lentilsoupwithsalmon,
            "https://victoriasidorova.wordpress.com/2019/02/13/lentil-soup-with-salmon-and-tomatoes/",
            ArrayList(),
            ArrayList()
        ),
        Dish(
            10,
            "Oatmeal pancakes",
            "Oatmeal pancakes",
            10,
            10,
            R.drawable.oatmeal_pancakes,
            "https://victoriasidorova.wordpress.com/2019/01/01/oatmeal-pancakes/",
            ArrayList(),
            ArrayList()
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
    val instructions:  ArrayList<String>
)
