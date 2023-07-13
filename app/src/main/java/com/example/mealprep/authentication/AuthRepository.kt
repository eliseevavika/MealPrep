package com.example.mealprep.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository {
    suspend fun signUpWithEmailAndPassword(email: String, password: String): SignUpResult {
        return try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            SignUpResult.Success(Unit)
        } catch (e: Exception) {
            SignUpResult.Error(e)
        }
    }

    suspend fun sendEmailVerification(): Boolean {
        try {
            Firebase.auth.currentUser?.sendEmailVerification()?.await()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}