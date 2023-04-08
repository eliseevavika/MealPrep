package com.example.mealprep

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationMain
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.meaprep.R

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(title) })
            .height(55.dp)
            .background(color = colorResource(id = R.color.white))
            .padding(start = 15.dp, top = 5.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = MealPrepColor.grey_600
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = title, color = Color.Black, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun BottomSheetContent(navController: NavHostController) {
    val context = LocalContext.current

    Column() {
        BottomSheetListItem(
            icon = R.drawable.ic_add_new,
            title = "Create new recipe",
            onItemClick = {
                context.startActivity(Intent(context, RecipeCreationMain::class.java))
            },
        )
        BottomSheetListItem(
            icon = R.drawable.ic_attach_file,
            title = "Save recipe link",
            onItemClick = {}
        )
    }
}