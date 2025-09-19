package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Signature(
    val id: String,
    val constatId: String,
    val driverId: String,
    val signatureType: SignatureType,
    val signatureData: String,
    @Contextual
    val createdAt: LocalDateTime
)