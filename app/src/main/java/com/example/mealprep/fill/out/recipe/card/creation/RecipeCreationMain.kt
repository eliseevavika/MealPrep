package com.example.mealprep.fill.out.recipe.card.creation

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mealprep.fill.out.recipe.card.TabScreen
import com.example.mealprep.fill.out.recipe.card.TopBarRecipeCreationForm

//class RecipeCreationMain : ContextA {
//    Scaffold(
//        topBar = { TopBarRecipeCreationForm(navController) },
//        content = { padding ->
//
//            Box(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
//                TabScreen(navController = navController)
//            }
//        },
//
//        backgroundColor = Color.White,
//
//        )
//}
class RecipeCreationMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val navController = rememberNavController()

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








