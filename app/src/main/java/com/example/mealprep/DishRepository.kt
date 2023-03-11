package com.example.littlelemon

import androidx.annotation.DrawableRes
import com.example.meaprep.R


object DishRepository {
    val dishes = listOf(
        Dish(
            1,
            "Barley Salad with Feta and... ",
            "Barley Salad with Feta and...",
            5,
            10,
            R.drawable.barleysalad
        ),
        Dish(
            2,
            "The Meatball Hot Dog",
            "The Meatball Hot Dog",
            10,
            15,
            R.drawable.hotdog
        ),
        Dish(
            3,
            "Indian lentil soup Masoor...",
            "Indian lentil soup Masoor...",
            10,
            30,
            R.drawable.masoordal
        ),
        Dish(
            4,
            "Chicken Cacciatore",
            "Chicken Cacciatore",
            15,
            60,
            R.drawable.cacciatore
        ),
        Dish(
            5,
            "Beetroot and orange salad",
            "Beetroot and orange salad",
            15,
            25,
            R.drawable.beetrootsalad
        ),
        Dish(
            6,
            "Frozen Yogurt Granola Cups",
            "Frozen Yogurt Granola Cups",
            15,
            120,
            R.drawable.frozenyougurt
        ),
        Dish(
            7,
            "Pasta farfalle or spirals with ...",
            "Pasta farfalle or spirals with ...",
            15,
            35,
            R.drawable.farfallepasta
        ),
        Dish(
            8,
            "Mini Caprese and Spanish...",
            "Mini Caprese and Spanish",
            10,
            30,
            R.drawable.minicaprese
        ),
        Dish(
            9,
            "Lentil soup with salmon and...",
            "Lentil soup with salmon and... .",
            15,
            45,
            R.drawable.lentilsoupwithsalmon
        ),
        Dish(
            10,
            "Oatmeal pancakes",
            "Oatmeal pancakes",
            10,
            10,
            R.drawable.oatmeal_pancakes
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
    @DrawableRes val imageResource: Int
)
