package smartcaravans.constat.client.settings.presentation.routes.theme

import smartcaravans.constat.client.settings.domain.models.ColorScheme
import smartcaravans.constat.client.settings.domain.models.Theme

data class UiState(
    val themeDialog: Boolean = false,
    val colorDialog: Boolean = false,
    val selectedTheme: Theme = Theme.SYSTEM,
    val selectedColor: ColorScheme = ColorScheme.BLUE,
)
