package smartcaravans.constat.client.auth.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Welcome : NavRoutes()

    @Serializable
    data object Login : NavRoutes()

    @Serializable
    data object IdentityTutorial : NavRoutes()
    @Serializable
    data object IdentityScan : NavRoutes()
    @Serializable
    data object IdentityVerification : NavRoutes()


    companion object {
        val routes = listOf<NavRoutes>(Welcome,  Login, IdentityTutorial, IdentityScan, IdentityVerification)
    }
}