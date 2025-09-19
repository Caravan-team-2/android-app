package smartcaravans.constat.client.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val message: String = "",
)
