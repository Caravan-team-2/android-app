package smartcaravans.constat.client.main.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Home : NavRoutes()
    @Serializable
    data object Constats : NavRoutes()
    @Serializable
    data object CreateConstat : NavRoutes()
    @Serializable
    data object Chat : NavRoutes()
    @Serializable
    data object Bot : NavRoutes()
}