package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val id: String,
    val make: String,
    val model: String,
    val type: String,
    val registrationNumber: String,
)
