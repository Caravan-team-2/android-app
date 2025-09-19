package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Contextual
import java.time.LocalDateTime

data class DriverLicense(
    val id: String,
    val number: String,
    val givenBy: String,
    val type: LicenseType,
    @Contextual
    val dateOfIssue: LocalDateTime
)
