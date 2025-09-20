package smartcaravans.constat.client.main.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarDefaults
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBar(
    route: Routes?,
    modifier: Modifier = Modifier,
    navigate: (NavRoutes) -> Unit,
) {
    ShortNavigationBar(
        modifier,
        windowInsets = ShortNavigationBarDefaults.windowInsets.add(
            WindowInsets(left = 8.dp, right = 8.dp)
        )
    ) {
        Routes.entries.forEach { screen ->
            val selected = route == screen
//            val weight by animateFloatAsState(if (selected) 1.4f else 1f)
            ShortNavigationBarItem(
                selected = selected,
                iconPosition = NavigationItemIconPosition.Start,
                icon = {
                    Icon(
                        if (selected) screen.selectedIcon else screen.unselectedIcon,
                        null,
                    )
                },
                label =
                    {
                        AnimatedVisibility(
                            selected,
                            enter = expandIn(expandFrom = Alignment.CenterStart),
                            exit = shrinkOut(shrinkTowards = Alignment.CenterStart)
                        ) {
                            Text(
                                stringResource(screen.title),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                onClick = {
                    navigate(screen.destination)
                },
                colors = ShortNavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(.2f),
                )
            )
        }
    }
}