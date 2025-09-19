package smartcaravans.constat.client.core.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.collections.firstOrNull

@Composable
fun<T, R> NavHostController.getCurrentRoute(
    entries: List<T>,
    callback: (T) -> R,
): T?
    = currentBackStackEntryAsState().value?.destination?.route?.let { route ->
        entries
            .firstOrNull {
                callback(it)?.javaClass?.kotlin?.qualifiedName?.contains(route) == true
            }
    }