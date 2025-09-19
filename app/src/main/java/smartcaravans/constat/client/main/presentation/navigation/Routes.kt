package smartcaravans.constat.client.main.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.ui.graphics.vector.ImageVector
import smartcaravans.constat.client.R

enum class Routes(
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destination: NavRoutes
) {
    Home(
        R.string.home,
        Icons.Default.Home,
        Icons.Outlined.Home,
        NavRoutes.Home
    ),
    Constats(
        R.string.constats,
        Icons.Default.DirectionsCar,
        Icons.Outlined.DirectionsCar,
        NavRoutes.Constats
    ),
    Chat(
        R.string.profile,
        Icons.Default.People,
        Icons.Outlined.People,
        NavRoutes.Chat
    ),
    Bot(
        R.string.bot,
        Icons.Default.QuestionAnswer,
        Icons.Outlined.QuestionAnswer,
        NavRoutes.Bot
    ),
}
