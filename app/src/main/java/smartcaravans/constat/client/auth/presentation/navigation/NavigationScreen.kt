package smartcaravans.constat.client.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import smartcaravans.constat.client.auth.presentation.routes.IdentityScan
import smartcaravans.constat.client.auth.presentation.routes.IdentityTutorial
import smartcaravans.constat.client.auth.presentation.routes.IdentityVerification
import smartcaravans.constat.client.auth.presentation.routes.LoginScreen
import smartcaravans.constat.client.auth.presentation.routes.WelcomeScreen
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationScreen(
    viewModel: AuthViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = NavRoutes.Welcome
    ) {
        composable<NavRoutes.Welcome> {
            WelcomeScreen {
                navController.navigate(NavRoutes.Login)
            }
        }
        composable<NavRoutes.Login> {
            LoginScreen(viewModel)
        }
        composable<NavRoutes.IdentityTutorial> {
            IdentityTutorial {
                navController.navigate(NavRoutes.IdentityScan)
            }
        }
        composable<NavRoutes.IdentityScan> {
            IdentityScan(viewModel)
        }
        composable<NavRoutes.IdentityVerification> {
            IdentityVerification(viewModel)
        }
    }
}