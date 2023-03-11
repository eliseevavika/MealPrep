package com.example.mealprep

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun TopAppBar(scaffoldState: ScaffoldState? = null, scope: CoroutineScope? = null) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp).fillMaxWidth(60f),
        verticalAlignment = Alignment.CenterVertically) {

        androidx.compose.material.IconButton(onClick = {
            scope?.launch { scaffoldState?.drawerState?.open() }
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
        Text(text = "Recipes", style = MaterialTheme.typography.body1, fontSize = 20.sp)
        androidx.compose.material.IconButton(onClick = { }) {
//            Image(
//                painter = painterResource(id = R.drawable.icons_clock2),
//                contentDescription = "Cart",
//                modifier = Modifier.size(24.dp)
//            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    TopAppBar()
}
