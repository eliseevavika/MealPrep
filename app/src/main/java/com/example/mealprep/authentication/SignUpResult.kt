package com.example.mealprep.authentication

sealed class SignUpResult {
    object Initial : SignUpResult()
    object Loading : SignUpResult()
    data class Success(val data: Unit) : SignUpResult()
    data class Error(val exception: Exception) : SignUpResult()
}