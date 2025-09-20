package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DriveEta
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.size.Size
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.MyImage
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveIconButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DamagesFormScreen(
    damagesFormState: DamagesFormState,
    onChange: (DamagesFormState) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.constat_form_car),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(50.dp),
            contentScale = ContentScale.Fit
        )
        DamagePlaces.entries.forEach {
            val selected = damagesFormState.selected.contains(it)
            ExpressiveIconButton(
                if (selected) Icons.Default.Info
                else Icons.Default.Check,
                size = IconButtonDefaults.largeIconSize,
                modifier = Modifier.align(it.alignment)
                    .size(60.dp)
                    .offset(it.offset.first, it.offset.second),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.surfaceContainer,
                )
            ) {
                onChange(
                    if (selected) damagesFormState.copy(
                        selected = damagesFormState.selected - it
                    ) else damagesFormState.copy(
                        selected = damagesFormState.selected + it
                    )
                )
            }
        }
    }
}