package com.example.mealprep.authentication

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginScreenViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    val isUserAuthenticated: MutableState<Boolean> = mutableStateOf(false)
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        checkUserAuthentication()
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }


    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        try {
            loadingState.emit(LoadingState.LOADING)
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            loadingState.emit(LoadingState.LOADED)
        } catch (e: Exception) {
            loadingState.emit(LoadingState.error(e.localizedMessage))
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

    fun checkUserAuthentication() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        isUserAuthenticated.value = currentUser != null
    }

    fun sendPasswordResetEmail(email: String, onEmailSentMessage: () -> Unit, onError: () -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onEmailSentMessage()
                } else {
                    onError()
                }
            }
    }
}