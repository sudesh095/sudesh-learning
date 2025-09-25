package com.sudesh.core

sealed class AppError(val message: String) {
    object Network : AppError("Network error")
    object Empty : AppError("No data available")
    class Unknown(msg: String) : AppError(msg)
}