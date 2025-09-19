package smartcaravans.constat.client.core.util

import kotlinx.serialization.Serializable
import smartcaravans.constat.client.core.domain.models.User

@Serializable
data class ApiData(
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: User? = null
)
