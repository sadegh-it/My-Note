package io.github.sadeghit.mynote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.sadeghit.mynote.ui.screen.add_edit.AddEditNoteScreen
import io.github.sadeghit.mynote.ui.screen.notes.NoteListScreen

@Composable
fun SetupNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.NoteListScreen.route
    ) {
        composable(Screens.NoteListScreen.route) {
            NoteListScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Screens.AddEditNoteScreen.createRoute(noteId))
                },
                onAddClick = {
                    navController.navigate(Screens.AddEditNoteScreen.createRoute())
                }
            )
        }

        composable(
            route = Screens.AddEditNoteScreen.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")
            AddEditNoteScreen(
                noteId = if (noteId == 0L) null else noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}