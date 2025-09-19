package smartcaravans.constat.client.main.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import smartcaravans.constat.client.core.presentation.CollectEvents
import smartcaravans.constat.client.core.util.Event
import smartcaravans.constat.client.core.util.getCurrentRoute
import smartcaravans.constat.client.main.presentation.navigation.NavigationBar
import smartcaravans.constat.client.main.presentation.navigation.NavigationScreen
import smartcaravans.constat.client.main.presentation.navigation.Routes
import smartcaravans.constat.client.main.presentation.viewmodel.MainViewModel
import smartcaravans.constat.client.ui.theme.AppTheme

@Composable
fun AppScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val route = navController.getCurrentRoute(Routes.entries) {
        it.destination
    }
    CollectEvents {
        when(it) {
            is Event.MainNavigate -> navController.navigate(it.route)
            else -> Unit
        }
    }
    Surface(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            AnimatedVisibility(route != null) {
                TopBar()
            }
            NavigationScreen(viewModel, navController, Modifier.fillMaxSize().weight(1f))
            AnimatedVisibility(route != null) {
                NavigationBar(route ?: Routes.Home) {
                    navController.navigate(it)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppScreenPreview() {
    AppTheme {
        AppScreen(viewModel())
    }
}