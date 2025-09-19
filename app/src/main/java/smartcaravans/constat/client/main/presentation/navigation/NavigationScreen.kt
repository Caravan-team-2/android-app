package smartcaravans.constat.client.main.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.util.Resource
import smartcaravans.constat.client.core.util.map
import smartcaravans.constat.client.main.presentation.navigation.routes.ConstatsScreen
import smartcaravans.constat.client.main.presentation.navigation.routes.CreateConstatScreen
import smartcaravans.constat.client.main.presentation.navigation.routes.HomeScreen
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiAction
import smartcaravans.constat.client.main.presentation.viewmodel.MainViewModel
import smartcaravans.constat.client.main.util.mapToVehicles
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
fun NavigationScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val vehicles = (uiState.insurranceResource as? Resource.Success)?.data?.mapToVehicles()
        ?: emptyList()
    val formState by viewModel.formState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home,
        modifier = modifier,
        enterTransition = {
            materialFadeThroughIn()
        },
        exitTransition = {
            materialFadeThroughOut()
        }
    ) {
        composable<NavRoutes.Home> {
            HomeScreen(
                uiState,
                viewModel::onAction
            )
        }
        composable<NavRoutes.Constats> {
            ConstatsScreen(
                uiState,
                onAction = viewModel::onAction
            )
        }
        composable<NavRoutes.CreateConstat> {
            CreateConstatScreen(
                formState,
                vehicles = vehicles,
                onClose = {
                    navController.navigateUp()
                },
                onNext = { viewModel.onAction(MainUiAction.ConstatDialogNext) },
                onPrev = { viewModel.onAction(MainUiAction.ConstatDialogBack) },
            ) {
                viewModel.onAction(MainUiAction.SetConstatFormState(it))
            }
        }
        composable<NavRoutes.Chat> {
        }
        composable<NavRoutes.Bot> {
        }
    }
}