package com.example.mealprep.fill.out.recipe.card.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mealprep.fill.out.recipe.card.TopBarForSettingsScreen
import com.example.mealprep.ui.navigation.BottomNavigationBar
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SettingsScreen(
    navController: () -> NavHostController,
    viewModal: () -> RecipeViewModel
) {
    Scaffold(topBar = {
        TopBarForSettingsScreen()
    },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {
                IconButton(onClick = { Firebase.auth.signOut() }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = null,
                    )
                }
            }
        })
}