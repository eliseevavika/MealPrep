package com.example.mealprep.authentication

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel, navController: NavHostController) {

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.loadingState.collectAsState()

    // Equivalent of onActivityResult
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signWithCredential(credential)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = {
                        Text(text = "Login")
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { Firebase.auth.signOut() }) {
                            Icon(
                                imageVector = Icons.Rounded.ExitToApp,
                                contentDescription = null,
                            )
                        }
                    }
                )
                if (state.status == LoadingState.Status.RUNNING) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = userEmail,
                        label = {
                            Text(text = "Email")
                        },
                        onValueChange = {
                            userEmail = it
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        value = userPassword,
                        label = {
                            Text(text = "Password")
                        },
                        onValueChange = {
                            userPassword = it
                        }
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
                        content = {
                            Text(text = "LOG IN")
                        },
                        onClick = {
                            viewModel.signInWithEmailAndPassword(
                                userEmail.trim(),
                                userPassword.trim()
                            )
                        }
                    )

                    ClickableText(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.caption,

                        text = AnnotatedString("Forgot your password?"),
                        onClick = {
                            navController.navigate(com.example.mealprep.ui.navigation.ForgotPasswordScreen.route)
                        })

                    Spacer(modifier = Modifier.padding(vertical = 18.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                        text = "Login with:"
                    )

                    val context = LocalContext.current

                    OutlinedButton(
                        border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken("876694761799-qsdked37mj4id8hoh1b1j4f3ggv8na04.apps.googleusercontent.com")
                                    .requestEmail()
                                    .build()

                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                        },
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                content = {
                                    Icon(
                                        tint = Color.Unspecified,
                                        painter = painterResource(id = com.google.android.gms.base.R.drawable.googleg_standard_color_18),
                                        contentDescription = null,
                                    )
                                    Text(
                                        style = MaterialTheme.typography.button,
                                        color = MaterialTheme.colors.onSurface,
                                        text = "Google"
                                    )
                                    Icon(
                                        tint = Color.Transparent,
                                        imageVector = Icons.Default.MailOutline,
                                        contentDescription = null,
                                    )
                                }
                            )
                        }
                    )

                    when (state.status) {
                        LoadingState.Status.SUCCESS -> {
                            Text(text = "Success")
                        }
                        LoadingState.Status.FAILED -> {
                            Text(text = state.msg ?: "Error")
                        }
                        else -> {}
                    }
                    Spacer(modifier = Modifier.padding(vertical = 18.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                        text = "Do you need an account?"
                    )

                    OutlinedButton(
                        border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {

                        },
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                content = {

                                    Text(
                                        color = MaterialTheme.colors.onSurface,
                                        text = "REGISTER"
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}
