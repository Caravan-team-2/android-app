package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Damage(
    val id: String,
    val constatId: String,
    val driverId: String,
    val description: String
)