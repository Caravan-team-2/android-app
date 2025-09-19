package smartcaravans.constat.client.core.util

import smartcaravans.constat.client.auth.presentation.navigation.NavRoutes

sealed interface Event {
    data class AuthNavigate(val route: NavRoutes): Event
    data class AuthShowMessage(val res: Int): Event
    data object LaunchMainActivity: Event
    data class MainNavigate(val route: smartcaravans.constat.client.main.presentation.navigation.NavRoutes): Event
}