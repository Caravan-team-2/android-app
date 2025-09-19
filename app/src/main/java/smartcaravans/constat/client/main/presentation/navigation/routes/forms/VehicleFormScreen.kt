package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.UserInsurrancesQuery
import smartcaravans.constat.client.core.presentation.MyTextField
import smartcaravans.constat.client.main.presentation.navigation.routes.VehicleCard
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiAction

@Composable
fun VehicleFormScreen(
    formState: VehicleInfoFormState,
    vehicles: List<UserInsurrancesQuery.Vehicle>,
    onStateChange: (VehicleInfoFormState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(vehicles, { index, it -> it.id }) { index, vehicle ->
                VehicleCard(vehicle, selected = formState.selectedVehicle == index) {
                    onStateChange(formState.copy(selectedVehicle = index))
                }
            }
            item {
                Card(
                    Modifier
                        .clickable {

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
        MyTextField(
            formState.comingFrom,
            {
                onStateChange(formState.copy(comingFrom = it))
            },
            label = stringResource(R.string.coming_from),
            error = formState.comingFromError,
        )
        MyTextField(
            formState.goingTo,
            {
                onStateChange(formState.copy(goingTo = it))
            },
            label = stringResource(R.string.going_to),
            error = formState.goingToError,
        )
    }

}