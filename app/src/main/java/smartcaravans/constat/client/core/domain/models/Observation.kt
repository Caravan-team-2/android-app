package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Observation(
    val id: String,
    val constatId: String,
    val driverId: String,
    val note: String
)