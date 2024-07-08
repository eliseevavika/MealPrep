package com.sliceup.mealprep.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sliceup.mealprep.ui.navigation.RecipeCreation
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(title) })
            .height(100.dp)
            .background(color = colorResource(id = R.color.white))
            .padding(start = 25.dp, top = 25.dp, bottom = 15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = MealPrepColor.grey_600,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                color = Color.Black,
                fontFamily = fontFamilyForBodyB2,
                fontSize = 19.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(
    navController: () -> NavHostController,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    Column {
        BottomSheetListItem(
            icon = R.drawable.ic_add_new,
            title = "Create new recipe",
            onItemClick = {
                scope.launch { modalBottomSheetState.hide() }
                navController().navigate(RecipeCreation.route)
            },
        )
    }
}