package smartcaravans.constat.client.auth.presentation

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import smartcaravans.constat.client.auth.presentation.navigation.NavigationScreen
import smartcaravans.constat.client.core.util.getCurrentRoute
import smartcaravans.constat.client.auth.presentation.navigation.NavRoutes
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthViewModel
import smartcaravans.constat.client.core.presentation.CollectEvents
import smartcaravans.constat.client.core.util.Event
import smartcaravans.constat.client.main.presentation.MainActivity
import smartcaravans.constat.client.settings.presentation.util.findActivity

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<AuthViewModel>()
    val navController = rememberNavController()
    val route = navController.getCurrentRoute(NavRoutes.routes) {
        it
    }
//    var viewHeight by remember { mutableIntStateOf(0) }
//    val viewHeightDp = with(LocalDensity.current) {
//        viewHeight.toDp()
//    }
    val backgroundHeightFraction by animateFloatAsState(
        when(route) {
            NavRoutes.Login -> .5f
            NavRoutes.IdentityTutorial -> .3f
            NavRoutes.IdentityScan -> .25f
            else -> 0f
        }
    )
    val background by animateColorAsState(
        when(route) {
            NavRoutes.Login -> MaterialTheme.colorScheme.surfaceContainer
            else -> MaterialTheme.colorScheme.background
        }
    )
    LaunchedEffect(route) {
        println("Current route: $route")
    }
//    val backgroundHeight = viewHeightDp * backgroundHeightFraction
    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    CollectEvents {
        when(it) {
            is Event.AuthNavigate -> {
                navController.navigate(it.route)
            }
            is Event.AuthShowMessage ->
                scope.launch {
                    snackBarState.showSnackbar(
                        "${context.getText(it.res)}",
                        null,
                        true
                    )
                }

            Event.LaunchMainActivity -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }

            else -> {}
        }
    }
    Scaffold(
        modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarState)
        },
        contentWindowInsets = WindowInsets()
    ) {
        Box(Modifier
            .padding(it)
            .background(background)
            .fillMaxSize()
            .onGloballyPositioned {
    //            viewHeight = it.size.height
            }
        ) {
            Box(
                Modifier.fillMaxWidth()
                    .fillMaxHeight(backgroundHeightFraction)
                    .background(MaterialTheme.colorScheme.primary)
            )
            NavigationScreen(viewModel, navController)
        }
    }
}