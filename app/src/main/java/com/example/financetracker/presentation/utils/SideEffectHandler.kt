package com.example.financetracker.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.SharedFlow

/**
 * A utility composable that handles side effects from a SharedFlow
 * This ensures that side effects are processed only once and in order
 */
@Composable
fun <T> SideEffectHandler(
    sideEffects: SharedFlow<T>,
    onSideEffect: (T) -> Unit
) {
    var lastSideEffect by remember { mutableStateOf<T?>(null) }
    
    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            if (sideEffect != lastSideEffect) {
                lastSideEffect = sideEffect
                onSideEffect(sideEffect)
            }
        }
    }
} 