package smartcaravans.constat.client.main.presentation.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.graphics.vector.ImageVector
import smartcaravans.constat.client.R

enum class ConstatSheetState(
    val buttonText: Int,
    val titleText: Int,
    val descriptionText: Int,
    val icon: ImageVector
) {
    NFC(
        buttonText = R.string.constat_nfc_button,
        titleText = R.string.constat_nfc_title,
        descriptionText = R.string.constat_nfc_description,
        icon = Icons.Default.Nfc
    ),
    QR_CODE(
        buttonText = R.string.constat_qr_code_button,
        titleText = R.string.constat_qr_code_title,
        descriptionText = R.string.constat_qr_code_description,
        icon = Icons.Default.QrCode
    )
}