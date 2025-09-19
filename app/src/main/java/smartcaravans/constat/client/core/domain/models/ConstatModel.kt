package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ConstatModel(
    val id: String,
    val driverAId: String,
    val driverBId: String,
    @Contextual
    val dateTime: LocalDateTime,
    val location: String,
    val injuredCount: Int,
    @Contextual
    val createdAt: LocalDateTime,
)
