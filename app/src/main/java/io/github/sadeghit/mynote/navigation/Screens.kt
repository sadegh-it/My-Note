package io.github.sadeghit.mynote.navigation

sealed class Screens(val route: String) {
    object NoteListScreen : Screens("note_list_screen")
    object AddEditNoteScreen : Screens("add_edit_note_screen/{noteId}") {
        fun createRoute(noteId: Long? = null): String {
            return "add_edit_note_screen/${noteId ?: 0}"
        }
    }


    fun routeWithArgs(vararg args: String) =
        buildString {
            append(route)
            args.forEachIndexed { index, _ ->
                append("?$index={$index}")

            }
        }

    fun paramsWithArgs(vararg args: String) =
        buildString {
            append(route)
            args.forEachIndexed { index, value ->
                append("?$index=$value")

            }
        }


}


