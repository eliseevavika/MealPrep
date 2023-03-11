package com.example.mealprep

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
//import com.example.littlelemon.ui.theme.LittleLemonColor

@Composable
fun UpperPanel() {
//    Column(
//        modifier = Modifier
//            .background(
//                MealPrepColor.green
//            )
//            .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
//
//    ) {
//        Text(
//            text = stringResource(id = R.string.title),
//            fontSize = 40.sp,
//            fontWeight = FontWeight.Bold,
//            color = MealPrepColor.yellow
//        )
//        Text(
//            text = stringResource(id = R.string.location),
//            fontSize = 24.sp,
//            color = MealPrepColor.cloud
//        )
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .padding(top = 20.dp)
//        ) {
//            Text(
//                text = stringResource(id = R.string.description),
//                style = MaterialTheme.typography.body1,
//                modifier = Modifier
//                    .padding(bottom = 28.dp, end = 20.dp)
//                    .fillMaxWidth(0.6f),
//                color = MealPrepColor.cloud
//            )
//            Image(
//                painter = painterResource(id = R.drawable.upperpanelimage),
//                contentDescription = "Upper Panel Image",
//                modifier = Modifier.clip(RoundedCornerShape(10.dp))
//            )
//        }
//        Button(
//            onClick = { }
//        ) {
//            Text(
//                text = stringResource(id = R.string.order_button_text)
//            )
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun UpperPanelPreview() {
    UpperPanel()
}
