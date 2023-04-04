package com.example.mealprep.fill.out.recipe.card.mealplanning

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealprep.BottomNavigationBar
import com.example.mealprep.MealPrepForSpecificDay
import com.example.mealprep.addEmptyLines
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB2
import com.example.meaprep.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalUnitApi::class, ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealPlanningScreen(
    navController: NavHostController,
    viewModel: MealPlanningViewModel,
    chosenDayId: Int?
) {
    val days = listOf(
        Day(0, DayOfWeek.Sunday),
        Day(1, DayOfWeek.Monday),
        Day(2, DayOfWeek.Tuesday),
        Day(3, DayOfWeek.Wednesday),
        Day(4, DayOfWeek.Thursday),
        Day(5, DayOfWeek.Friday),
        Day(6, DayOfWeek.Saturday)
    )

   var chosenDay by remember { mutableStateOf(days[0]) }

    val chosenMealsForMealPrepByDay =
        viewModel.list.observeAsState().value


    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    var isSheetFullScreen by remember { mutableStateOf(false) }
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp
    val modifier = if (isSheetFullScreen)
        Modifier
            .fillMaxSize()
    else
        Modifier.fillMaxWidth()

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        ),
        sheetContent = {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
             BottomSheetContent(navController, viewModel, chosenDay)
            }
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBarMealPlanning()
            },
            bottomBar = { BottomNavigationBar(navController = navController) },
            content = { padding ->
                Box(modifier = Modifier.padding(16.dp)) {
                    LazyColumn {
                        if (!days.isNullOrEmpty()) {
                            items(days) { day ->
                                Column(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillParentMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .padding(
                                                start = 16.dp,
                                                top = 30.dp,
                                                end = 16.dp,
                                                bottom = 30.dp
                                            )
                                            .clickable(onClick = {
                                                coroutineScope.launch {
                                                    if (modalSheetState.isVisible) {
                                                        modalSheetState.hide()
                                                    } else {
                                                        chosenDay = day
                                                        modalSheetState.animateTo(
                                                            ModalBottomSheetValue.Expanded
                                                        )
                                                    }
                                                }
                                            })
                                    ) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .weight(7f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.outline_density_medium_24),
                                                tint = MealPrepColor.grey_600,
                                                contentDescription = "Icon",
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(width = 8.dp))
                                            Text(
                                                text = day.name.toString(),
                                                fontFamily = fontFamilyForBodyB2,
                                                fontSize = 16.sp
                                            )

                                        }
                                    }


                                    if (!chosenMealsForMealPrepByDay.isNullOrEmpty() && chosenDayId == day.id) {
                                            LazyRow(modifier = Modifier.fillMaxHeight()) {
                                                items(chosenMealsForMealPrepByDay) { dish ->
                                                    Card(
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .wrapContentSize(),
                                                        onClick = {
//
                                                        }) {
                                                        Row {
                                                            Column(
                                                                modifier = Modifier
                                                                    .size(74.dp, 108.dp),
                                                                verticalArrangement = Arrangement.Center,
                                                                horizontalAlignment = Alignment.Start,

                                                                ) {
                                                                Image(
                                                                    painter = painterResource(id = dish.imageResource),
                                                                    contentDescription = "Image",
                                                                    contentScale = ContentScale.Crop,
                                                                    modifier = Modifier
                                                                        .size(74.dp, 74.dp)
                                                                        .clip(
                                                                            RoundedCornerShape(16.dp)
                                                                        )
                                                                )

                                                                Text(
                                                                    text = dish.name.addEmptyLines(2),
                                                                    maxLines = 2,
                                                                    style = MaterialTheme.typography.body1,
                                                                    fontSize = 10.sp,
//                                                                modifier = Modifier.padding(start = 16.dp)
                                                                )
                                                            }
                                                        }
                                                    }

                                                }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}


@Composable
fun BottomSheetContent(
    navController: NavHostController,
    viewModel: MealPlanningViewModel,
    chosenDay: Day
) {
    Column() {
        BottomSheetListItem(
            icon = R.drawable.outline_edit_24,
            title = "Add / Edit plan for ${chosenDay.name}",
            onItemClick = {
                navController?.navigate(MealPrepForSpecificDay.route + "/${chosenDay.id}")
            }
        )
        BottomSheetListItem(
            icon = R.drawable.outline_delete_24,
            title = "Reset menu",
            onItemClick = {
                viewModel.list.value = null
            }
        )
    }
}

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(title) })
            .height(55.dp)
            .background(color = colorResource(id = R.color.white))
            .padding(start = 15.dp, top = 5.dp), verticalAlignment = Alignment.CenterVertically,
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



