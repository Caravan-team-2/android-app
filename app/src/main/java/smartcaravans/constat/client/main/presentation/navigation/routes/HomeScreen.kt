package smartcaravans.constat.client.main.presentation.navigation.routes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.GetConstatsQuery
import smartcaravans.constat.client.R
import smartcaravans.constat.client.UserInsurrancesQuery
import smartcaravans.constat.client.core.presentation.IconContainer
import smartcaravans.constat.client.core.presentation.ListContainer
import smartcaravans.constat.client.core.presentation.ResourceContainer
import smartcaravans.constat.client.core.presentation.ShadowContainer
import smartcaravans.constat.client.core.util.map
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiAction
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiState
import smartcaravans.constat.client.main.util.mapToVehicles
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveIconButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    uiState: MainUiState,
    onAction: (MainUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val constats = uiState.constatsResource
    val vehicles = uiState.insurranceResource.map {
        it.mapToVehicles()
    }
    LazyColumn(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        item {
            Card(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Row(Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Image(
                            painterResource(R.drawable.home_screen_car),
                            null,
                            Modifier.fillMaxHeight().padding(vertical = 16.dp),
                            contentScale = ContentScale.FillHeight
                        )
                        Column(Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                stringResource(R.string.renew_your_insurance_plan),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                stringResource(R.string.enjoy_more_coverage_on_the_insurance),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                            ExpressiveButton(
                                stringResource(R.string.review_now),
                                size = ButtonDefaults.ExtraSmallContainerHeight,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .height(40.dp)
                                    .fillMaxWidth(.8f)
                            ) { }
                        }
                    }
                }
            }
        }
        item {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.your_insured_cars),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                ExpressiveButton(
                    stringResource(R.string.see_all),
                    size = ButtonDefaults.ExtraSmallContainerHeight,
                    colors = ButtonDefaults.textButtonColors(),
                ) { }
            }
        }
        item {
            ResourceContainer(vehicles) { vehicles ->
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(vehicles, { it.id }) {
                        VehicleCard(it)
                    }
                    item {
                        Card(
                            Modifier
                                .clickable {
                                    onAction(MainUiAction.ShowAddVehicleDialog)
                                }
                                .width(200.dp)
                                .fillMaxHeight(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.heightIn(min = 200.dp).fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    null,
                                    Modifier.size(40.dp)
                                )
                                Text(
                                    stringResource(R.string.add_vehicle),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.constats),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                ExpressiveButton(
                    stringResource(R.string.see_all),
                    size = ButtonDefaults.ExtraSmallContainerHeight,
                    colors = ButtonDefaults.textButtonColors(),
                ) { }
            }
        }
        item {
            ResourceContainer(constats, Modifier.heightIn(200.dp)) { constats ->
                ListContainer(
                    constats.take(3),
                    Modifier.heightIn(200.dp).fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    ConstatListItem(it)
                }
            }
        }
    }
    AddVehicleDialog(
        uiState.createVehicleDialogState,
        {
            onAction(MainUiAction.SetAddVehicleDialogState(it))
        },
        uiState.dialogVisible,
        {

        },
    ) {
        onAction(MainUiAction.HideAddVehicleDialog)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VehicleCard(
    vehicle: UserInsurrancesQuery.Vehicle,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = { }
) {
    val borderColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant
    )
    val containerColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface
    )
    val contentColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface
    )
    Surface(
        modifier = modifier.clickable { onClick() },
        border = BorderStroke(1.dp, borderColor.copy(0.2f)),
        shadowElevation = 8.dp,
        contentColor = contentColor,
        color = containerColor,
    ) {
        Column(
            Modifier
                .width(200.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painterResource(R.drawable.car),
                null,
                Modifier.fillMaxWidth().height(100.dp)
            )
            Text(
                vehicle.model,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConstatListItem(
    constat: GetConstatsQuery.Data1,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconContainer(
                Icons.Default.ContentPaste,
                iconTint = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            )
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    constat.location,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                Text(
                    "${constat.dateTime} - ${constat.dateTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
            }
            ExpressiveIconButton(
                Icons.Default.Download,
                size = IconButtonDefaults.smallIconSize,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {

            }
        }
    }
}
