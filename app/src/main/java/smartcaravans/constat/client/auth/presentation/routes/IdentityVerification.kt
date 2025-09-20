package smartcaravans.constat.client.auth.presentation.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import smartcaravans.constat.client.R
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthUiAction
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthViewModel
import smartcaravans.constat.client.core.presentation.MyImage
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IdentityVerification(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(Modifier.fillMaxWidth()) {
            MyImage(
                uiState.scannedImage?.toUri(),
                Icons.Default.CreditCard,
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.586f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.after_detected_your_photo),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val checklistItems = stringResource(R.string.make_sure_your_id_card).split("\n")
                checklistItems.forEach { item ->
                    if (item.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = item.trim(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.please_confirm_that),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.id_in_not_expired),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Column(Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ExpressiveButton(
                stringResource(R.string.confirm),
                ButtonDefaults.MediumContainerHeight,
                Modifier.fillMaxWidth()
            ) {
                viewModel.onAction(AuthUiAction.Upload)
            }
            ExpressiveButton(
                stringResource(R.string.retake),
                ButtonDefaults.MediumContainerHeight,
                Modifier.fillMaxWidth(),
                outlined = true
            ) { }
        }
    }
}
