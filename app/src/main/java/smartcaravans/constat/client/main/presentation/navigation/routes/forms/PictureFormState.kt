package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import java.io.File

data class PictureFormState(
    val pictures: List<File> = emptyList(),
    val isLoading: Boolean = false
)
