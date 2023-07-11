package com.example.mealprep.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = {
                Text(text = "Email")
            },
            onValueChange = {
                email = it
            }
        )

        Spacer(modifier = Modifier.padding(18.dp))
        Button(
            onClick = {
                sendPasswordResetEmail(email.text.trim())
            }
        ) {
            Text(
                text = "Reset password",
                fontSize = 15.sp
            )
        }
    }
}