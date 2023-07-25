package com.example.mealprep.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.ui.theme.fontFamilyForBodyB1
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.job

@Composable
fun SignUpScreen(
    viewModel: LoginScreenViewModel,
    auth: FirebaseAuth,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SignUpTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            SignUpContent(
                viewModel,
                padding = padding,
                navigateBack = navigateBack
            )
        }
    )

    SignUp(
        viewModel,
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        errorMessage = { error ->
            android.app.AlertDialog.Builder(context)
                .setMessage(error)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        })
}

@Composable
fun SignUp(
    viewModel: LoginScreenViewModel,
    sendEmailVerification: (String) -> Unit,
    errorMessage: (String) -> Unit
) {
    val signUpResponse = viewModel.signUpResult.collectAsState().value

    when (signUpResponse) {
        is SignUpResult.Initial -> {
        }
        is SignUpResult.Loading -> {
        }
        is SignUpResult.Success -> {
            sendEmailVerification("An email has been sent to your address. Please check your inbox to verify your account.")
        }
        is SignUpResult.Error -> {
            val exception = signUpResponse.exception
            var error = viewModel.showWarningMessage(exception)
            errorMessage(error)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpContent(
    viewModel: LoginScreenViewModel,
    padding: PaddingValues,
    navigateBack: () -> Unit
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
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = ""
                )
            )
        }
    )
    val context = LocalContext.current

    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "PERSONAL DETAILS", fontFamily = fontFamilyForBodyB1,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        EmailField(
            email = email,
            onEmailValueChange = { newValue ->
                email = newValue
            }
        )
        PasswordField(
            password = password,
            onPasswordValueChange = { newValue ->
                password = newValue
            }
        )
        Spacer(modifier = Modifier.padding(3.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            content = {
                Text(text = "SIGN UP", color = MealPrepColor.white, fontSize = 15.sp)
            },
            enabled = email.text.isNotEmpty() && password.text.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.orange),
            onClick = {
                keyboard?.hide()
                viewModel.signUp(
                    email.text, password.text,
                    onSuccess = {
                       viewModel.sendEmailVerification()
                        android.app.AlertDialog.Builder(context)
                            .setMessage(it)
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()

                    },
                    onError = {
                        android.app.AlertDialog.Builder(context)
                            .setMessage(it)
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    })
            }
        )
        Spacer(modifier = Modifier.padding(18.dp))
        Text(
            modifier = Modifier.clickable {
                navigateBack()
            },
            text = "Already a user?",
            fontSize = 15.sp
        )
    }
}

@Composable
fun PasswordField(
    password: TextFieldValue,
    onPasswordValueChange: (newValue: TextFieldValue) -> Unit
) {
    var passwordIsVisible by remember { mutableStateOf(false) }

    val focusRequester = FocusRequester()

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(MealPrepColor.transparent)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MealPrepColor.transparent,
            cursorColor = MealPrepColor.black,
            focusedIndicatorColor = MealPrepColor.black,
            unfocusedIndicatorColor = MealPrepColor.black,
            focusedLabelColor = MealPrepColor.grey_800,
            unfocusedLabelColor = MealPrepColor.grey_800
        ),
        singleLine = true,
        value = password,
        label = {
            Text(text = "Password")
        },
        onValueChange = { newValue ->
            onPasswordValueChange(newValue)
        },
        visualTransformation = if (passwordIsVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val icon = if (passwordIsVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(
                onClick = {
                    passwordIsVisible = !passwordIsVisible
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun EmailField(
    email: TextFieldValue,
    onEmailValueChange: (newValue: TextFieldValue) -> Unit
) {
    val focusRequester = FocusRequester()

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(MealPrepColor.transparent)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MealPrepColor.transparent,
            cursorColor = MealPrepColor.black,
            focusedIndicatorColor = MealPrepColor.black,
            unfocusedIndicatorColor = MealPrepColor.black,
            focusedLabelColor = MealPrepColor.grey_800,
            unfocusedLabelColor = MealPrepColor.grey_800
        ),
        singleLine = true,
        value = email,
        label = {
            Text(text = "Email")
        },
        onValueChange = { newValue ->
            onEmailValueChange(newValue)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
    )

    LaunchedEffect(Unit) {
        coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }
}