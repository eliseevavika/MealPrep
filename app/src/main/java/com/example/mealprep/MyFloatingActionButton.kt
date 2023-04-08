package com.example.mealprep

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mealprep.ui.theme.MealPrepColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyFloatingActionButton(scope: CoroutineScope, state: ModalBottomSheetState) {
    FloatingActionButton(
        modifier = Modifier
            .padding(all = 16.dp),
        backgroundColor = MealPrepColor.orange,
        onClick = {
            scope.launch {
                state.show()
            }
        }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = MealPrepColor.white)
    }
}