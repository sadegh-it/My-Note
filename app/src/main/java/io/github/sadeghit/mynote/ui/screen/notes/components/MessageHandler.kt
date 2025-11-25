package io.github.sadeghit.mynote.ui.screen.notes.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.sadeghit.mynote.ui.event.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MessageHandler(
    eventFlow: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest { event ->
            if (event is UiEvent.ShowMessage) {
                snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel,
                    duration = if (event.actionLabel != null) SnackbarDuration.Long else SnackbarDuration.Short
                ).let { result ->
                    if (result == SnackbarResult.ActionPerformed) {
                        event.onActionPerformed?.invoke()
                    }
                }
            }
        }
    }
}