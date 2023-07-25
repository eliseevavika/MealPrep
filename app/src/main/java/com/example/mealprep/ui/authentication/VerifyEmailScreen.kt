package com.example.mealprep.ui.authentication

import android.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.mealprep.ui.theme.MealPrepColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@Composable
fun VerifyEmailScreen(
    viewModel: LoginScreenViewModel, auth: FirebaseAuth, navigateToProfileScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = {},
            backgroundColor = MealPrepColor.transparent,
            contentColor = MealPrepColor.black,
            navigationIcon = {
            })
    }, content = { padding ->
        VerifyEmailContent(
            coroutineScope, viewModel, padding = padding,
            reloadUser = {
                viewModel.reloadUser()
            },
        )
    }, scaffoldState = scaffoldState
    )
    ReloadUser(
        viewModel,
        navigateToProfileScreen = {
            if (viewModel.isEmailVerified) {
                navigateToProfileScreen()
            } else {
                AlertDialog.Builder(context).setMessage("Your email is not verified.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        }
    )
}

@Composable
fun VerifyEmailContent(
    coroutineScope: CoroutineScope,
    viewModel: LoginScreenViewModel,
    padding: PaddingValues,
    reloadUser: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(start = 32.dp, end = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClickableText(modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.caption,
            text = AnnotatedString("Already Verified?"),
            onClick = {
                reloadUser()
            })
    }
}


@Composable
fun ReloadUser(
    viewModel: LoginScreenViewModel,
    navigateToProfileScreen: () -> Unit
) {
    when (val reloadUserResponse = viewModel.reloadUserResponse) {
        is Response.Loading -> " ProgressBar()"
        is Response.Success<Boolean> -> {
            val isUserReloaded = reloadUserResponse.data
            LaunchedEffect(isUserReloaded) {
                if (isUserReloaded) {
                    navigateToProfileScreen()
                }
            }
        }
        is Response.Failure -> reloadUserResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}