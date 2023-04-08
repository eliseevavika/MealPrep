package com.example.mealprep.fill.out.recipe.card.creation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mealprep.fill.out.recipe.card.TabScreen
import com.example.mealprep.fill.out.recipe.card.TopBarRecipeCreationForm

class RecipeCreationMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = { TopBarRecipeCreationForm() },
                content = { padding ->

                    Box(modifier = Modifier.padding(padding)) {
                        TabScreen()
                    }
                },
                backgroundColor = Color.White,
            )
        }
    }
}