package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
class Circumstance(
    val id: String,
    val constatId: String,
    val driverId: String,
    val code: Float
)