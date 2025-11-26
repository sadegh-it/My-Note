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
    LaunchedEffect(eventFlow, snackbarHostState) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessage -> {

                    val duration = if (event.actionLabel != null) {
                        SnackbarDuration.Long
                    } else {
                        SnackbarDuration.Short
                    }

                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = duration
                    )


                    if (result == SnackbarResult.ActionPerformed) {
                        event.onActionPerformed?.invoke()
                    }
                }


                else -> Unit
            }
        }
    }
}