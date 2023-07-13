package com.example.mealprep.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.example.mealprep.ui.theme.fontFamilyForBodyB2

@Composable
fun ForgotPasswordContent(
    padding: PaddingValues,
    sendPasswordResetEmail: (email: String) -> Unit,
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = ""
                )
            )
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RECOVER PASSWORD", fontFamily = fontFamilyForBodyB1,
            fontSize = 20.sp, textAlign = TextAlign.Start
        )

        Text(
            text = "We will send you an email with instructions to recover it.",
            fontFamily = fontFamilyForBodyB2,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.padding(3.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            value = email,
            label = {
                Text(text = "Email")
            },
            onValueChange = {
                email = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MealPrepColor.transparent,
                cursorColor = MealPrepColor.black,
                focusedIndicatorColor = MealPrepColor.black,
                unfocusedIndicatorColor = MealPrepColor.black,
                focusedLabelColor = MealPrepColor.grey_800,
                unfocusedLabelColor = MealPrepColor.grey_800
            ),
        )

        Spacer(modifier = Modifier.padding(3.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .height(50.dp),
            enabled = email.text.isNotEmpty(),
            content = {
                Text(text = "RECOVER PASSWORD", color = MealPrepColor.white)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.orange),
            onClick = {
                sendPasswordResetEmail(email.text.trim())
            }
        )
    }
}