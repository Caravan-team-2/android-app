package smartcaravans.constat.client.auth.presentation.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthUiAction
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthViewModel
import smartcaravans.constat.client.core.presentation.CameraView

@Composable
fun IdentityScan(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(.25f)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
        ) {
            Text(
                stringResource(R.string.id_scan_text),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        CameraView { file, _ ->
            viewModel.onAction(AuthUiAction.IdentityScanned(file))
        }
    }
}
