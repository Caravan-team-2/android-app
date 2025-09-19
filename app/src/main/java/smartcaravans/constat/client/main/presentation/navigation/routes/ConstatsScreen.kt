package smartcaravans.constat.client.main.presentation.navigation.routes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.domain.models.ConstatModel
import smartcaravans.constat.client.core.presentation.IconContainer
import smartcaravans.constat.client.core.presentation.ListContainer
import smartcaravans.constat.client.core.presentation.ResourceContainer
import smartcaravans.constat.client.main.presentation.viewmodel.ConstatSheetState
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiAction
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiState
import smartcaravans.constat.client.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConstatsScreen(
    uiState: MainUiState,
    modifier: Modifier = Modifier,
    onAction: (MainUiAction) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val menu = listOf(
        R.string.im_the_owner to {
            onAction(MainUiAction.SetConstatSheetState(true, ConstatSheetState.NFC))
        },
        R.string.someone_else to {
            onAction(MainUiAction.SetConstatSheetState(false, ConstatSheetState.QR_CODE))
        },
    )
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButtonMenu(
                expanded,
                {
                    ToggleFloatingActionButton(
                        expanded,
                        {
                            expanded = !expanded
                        },
                        containerSize = ToggleFloatingActionButtonDefaults.containerSizeMedium()
                    ) {
                        val angle by animateFloatAsState(
                            if (expanded) 45f else 0f
                        )
                        Icon(
                            Icons.Default.Add,
                            null,
                            Modifier.rotate(angle)
                                .animateIcon(
                                    { checkedProgress },
                                    size = ToggleFloatingActionButtonDefaults.iconSizeMedium()
                                )
                            ,
                        )
                    }
                }
            ) {
                menu.forEach { (text, action) ->
                    FloatingActionButtonMenuItem(
                        text = { Text(stringResource(text)) },
                        icon = {},
                        onClick = {
                            expanded = false
                            action()
                        },
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets()
    ) {
        ResourceContainer(uiState.constatsResource) { constats ->
            AnimatedContent(constats.isEmpty(), Modifier
                .padding(it)
                .fillMaxSize()) { empty ->
                if (empty) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconContainer(Icons.Default.ContentPaste, Modifier.size(200.dp))
                        Text(
                            stringResource(R.string.aucun_constat_pour_le_moment),
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(16.dp, 32.dp)
                    ) {
                        item {
                            Text(
                                stringResource(R.string.constats),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        item {
                            ListContainer(
                                constats.take(3),
                                Modifier.fillMaxWidth()
                            ) {
                                ConstatListItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
    ConstatSheet(
        uiState.qrCodeText,
        uiState.constatSheetState,
        uiState.owner,
        onSheetStateChange = {
            onAction(MainUiAction.SetConstatSheetState(uiState.owner, it))
        },
        onSkip = {
            onAction(MainUiAction.SkipScan)
        }
    ) {
        onAction(MainUiAction.Scanned(it))
    }
}

@Preview
@Composable
private fun EmptyConstatsScreenPreview() {
    var uiState by remember {
        mutableStateOf(MainUiState())
    }
    AppTheme {
        ConstatsScreen(uiState) {
            if (it is MainUiAction.SetConstatSheetState) {
                uiState = uiState.copy(constatSheetState = it.state)
            }
        }
    }
}