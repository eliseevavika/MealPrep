package com.example.mealprep.authentication

import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ForgotPasswordScreen(
    viewModel: LoginScreenViewModel
//    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
//                navigateBack = navigateBack
            )
        },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email, onEmailSentMessage = {
                        Toast.makeText(
                            context,
                            "You will receive an email shortly. Please follow the instructions to reset your password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }, onError =
                    {
                        Toast.makeText(
                            context,
                            "Failed to send password reset email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                }
            )
        }
    )
}

@Composable
fun ForgotPasswordTopBar(
//    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Forgot password screen"
            )
        },
        navigationIcon = {
//            BackIcon(
//                navigateBack = navigateBack
//            )
        }
    )
}