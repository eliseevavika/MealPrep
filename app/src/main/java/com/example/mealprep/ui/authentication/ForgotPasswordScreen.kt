package com.example.mealprep.ui.authentication

import android.app.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.mealprep.BackIcon
import com.example.mealprep.ui.theme.MealPrepColor

@Composable
fun ForgotPasswordScreen(
    viewModel: LoginScreenViewModel,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email, onEmailSentMessage = {
                        AlertDialog.Builder(context)
                            .setMessage("You will receive an email shortly. Please follow the instructions to reset your password.")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                navigateBack()
                            }
                            .show()
                    }, onError =
                    {
                        AlertDialog.Builder(context)
                            .setMessage(it)
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    )
                }
            )
        }
    )
}

@Composable
fun ForgotPasswordTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
        },
        backgroundColor = MealPrepColor.transparent,
        contentColor = MealPrepColor.black,
        navigationIcon = {
            BackIcon(
                navigateBack = navigateBack
            )
        }
    )
}