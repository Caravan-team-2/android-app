package smartcaravans.constat.client.auth.domain.dto

import kotlinx.serialization.Serializable
import smartcaravans.constat.client.core.domain.models.User

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
)
