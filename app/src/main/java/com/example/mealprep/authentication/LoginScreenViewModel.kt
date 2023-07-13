package com.example.mealprep.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginScreenViewModel : ViewModel() {
    private val authRepository: AuthRepository

    val loadingState = MutableStateFlow(LoadingState.IDLE)
    val isUserAuthenticated: MutableState<Boolean> = mutableStateOf(false)
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        checkUserAuthentication()
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
        authRepository = AuthRepository()
    }

    fun signInWithEmailAndPassword(email: String, password: String, onError: (String) -> Unit) =
        viewModelScope.launch {
            try {
                Firebase.auth.signInWithEmailAndPassword(email, password).await()
            } catch (e: Exception) {
                onError(e.message.toString())
            }
        }

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingState.emit(LoadingState.LOADING)
            Firebase.auth.signInWithCredential(credential).await()
            loadingState.emit(LoadingState.LOADED)
        } catch (e: Exception) {
            loadingState.emit(LoadingState.error(e.localizedMessage))
        }
    }

    var sendEmailVerificationResponse by mutableStateOf(false)
        private set

    private val _signUpResult = MutableStateFlow<SignUpResult>(SignUpResult.Initial)

    val signUpResult: StateFlow<SignUpResult> = _signUpResult.asStateFlow()

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _signUpResult.value = SignUpResult.Loading
            val result = authRepository.signUpWithEmailAndPassword(email, password)
            _signUpResult.value = result
        }
    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = authRepository.sendEmailVerification()
    }


    fun checkUserAuthentication() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        isUserAuthenticated.value = currentUser != null
    }

    fun sendPasswordResetEmail(
        email: String,
        onEmailSentMessage: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (email != "") {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onEmailSentMessage()
                    } else {
                        var exception = task.exception
                        val errorMessage = when (exception) {
                            is FirebaseAuthInvalidUserException -> "The email address is not associated with any account."
                            is FirebaseAuthInvalidCredentialsException -> "Invalid email format or authentication credentials."
                            else -> "An error occurred during the password reset process. Please try again later."
                        }
                        onError(errorMessage)
                    }
                }
        }
    }

    fun showWarningMessage(error: Exception): String {
        val errorMessage = when (error) {
            is FirebaseAuthUserCollisionException -> "The email address is already in use by another account."
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password format."
            else -> "An error occurred during sign-up. Please try again later."
        }
        return errorMessage
    }
}