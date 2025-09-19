package smartcaravans.constat.client.auth.presentation

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.auth.presentation.util.Permissions
import smartcaravans.constat.client.core.presentation.IconContainer
import smartcaravans.constat.client.core.presentation.TitleText
import smartcaravans.constat.client.core.presentation.util.expressiveRectShape
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveIconButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PermissionsDialog(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(
        Permissions.getGrantedPermissions(context).size < Permissions.entries.size
    ) }
    var grantedPermissions by remember {
        mutableStateOf(Permissions.getGrantedPermissions(context))
    }
    var deniedPermissions by remember {
        mutableStateOf(Permissions.getDeniedPermissions(context))
    }
    val allGranted = grantedPermissions.size == Permissions.entries.size
    val launcher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        permissions.forEach { (permission, granted) ->
            Permissions.fromPermissionString(permission)?.let {
                if (granted) grantedPermissions += it
                else grantedPermissions -= it
            }
        }
    }
    val grantAll = {
        val notGrantedPermissions =
            Permissions.entries.filter { it !in grantedPermissions }
                .map { it.permissionString }.toTypedArray()
        if (notGrantedPermissions.isNotEmpty()) {
            launcher.launch(notGrantedPermissions)
        }
    }
    LaunchedEffect(Unit) {
        if (allGranted) visible = false
        else if (deniedPermissions.isNotEmpty())

            context.startActivity(
                Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    android.net.Uri.fromParts(
                        "package",
                        context.packageName,
                        null
                    )
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        else grantAll()
    }

    if (visible) {
        ModalBottomSheet(
            onDismissRequest = { /* Disable dismiss */ },
            modifier = modifier,
            sheetState = rememberModalBottomSheetState(true) {
                it != SheetValue.Hidden
            }
        ) {
            LazyColumn(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    TitleText(
                        text = stringResource(R.string.permissions),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                itemsIndexed(Permissions.entries) { index, permission ->
                    val granted = grantedPermissions.contains(permission)
                    val denied = deniedPermissions.contains(permission)
                    PermissionItem(
                        permission = permission,
                        granted = granted,
                        denied = denied,
                        onClick = {
                            if (denied)
                                context.startActivity(
                                    Intent(
                                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        android.net.Uri.fromParts(
                                            "package",
                                            context.packageName,
                                            null
                                        )
                                    ).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                )
                            else
                                launcher.launch(arrayOf(permission.permissionString))
                        },
                        shape = expressiveRectShape(index, Permissions.entries.size)
                    )
                }
                item {
                    Row(
                        Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(2) { index ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val pressed by interactionSource.collectIsPressedAsState()
                            val weight by animateFloatAsState(
                                if (pressed) 1.5f else 1f
                            )
                            ExpressiveButton(
                                text =
                                    if (index == 0) stringResource(R.string.close)
                                    else if (allGranted) stringResource(R.string.all_set)
                                    else stringResource(R.string.grant_all),
                                size = ButtonDefaults.MediumContainerHeight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .weight(weight),
                                outlined = index == 0,
                                interactionSource = interactionSource,
                            ) {
                                if (index == 0)
                                    visible = false
                                else grantAll()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PermissionItem(
    permission: Permissions,
    granted: Boolean,
    denied: Boolean,
    onClick: () -> Unit,
    shape: Shape,
) {
    val containerColor by animateColorAsState(
        if (granted) MaterialTheme.colorScheme.primary
        else if (denied) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.surfaceContainerHigh,
    )
    val contentColor by animateColorAsState(
        if (granted) MaterialTheme.colorScheme.onPrimary
        else if (denied) MaterialTheme.colorScheme.onError
        else MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Card(
        shape = shape,
        onClick = onClick,
        colors =
            if (granted)
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            else if (denied)
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            else
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconContainer(
                icon = permission.icon,
                modifier = Modifier.size(60.dp),
                background = containerColor,
                iconTint = contentColor,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(permission.titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(permission.messageRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalContentColor.current.copy(.7f)
                )
            }
            IconContainer(
                icon = if (granted) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                modifier = Modifier.size(40.dp),
                background =
                    if (granted) MaterialTheme.colorScheme.primary
                    else if (denied) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                iconTint =
                    if (granted) MaterialTheme.colorScheme.onPrimary
                    else if (denied) MaterialTheme.colorScheme.onError
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            )
        }

    }
}