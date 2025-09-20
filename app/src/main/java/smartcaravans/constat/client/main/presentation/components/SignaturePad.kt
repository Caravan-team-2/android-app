package smartcaravans.constat.client.main.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

@Composable
fun SignaturePad(
    label: String,
    modifier: Modifier = Modifier,
    ratio: Float = 16 / 9f,
    onChange: (String?) -> Unit = {}
) {
    var signaturePadAdapter: SignaturePadAdapter? = null
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(Modifier.fillMaxWidth().aspectRatio(ratio)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(0.2f)),
            ) {
                SignaturePadView(
                    modifier = Modifier.fillMaxSize(),
                    onReady = { signaturePadAdapter = it },
                    onSigned = { onChange(signaturePadAdapter?.getSignatureSvg()) }
                )
            }
            SmallFloatingActionButton(
                { signaturePadAdapter?.clear() },
                Modifier.align(Alignment.BottomEnd).padding(8.dp)
            ) {
               Icon(Icons.Default.Delete, null)
            }
        }
    }
}