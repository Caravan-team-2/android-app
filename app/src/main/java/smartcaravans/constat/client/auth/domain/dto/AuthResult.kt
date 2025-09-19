package smartcaravans.constat.client.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthResult {
    @Serializable
    data class Success(val authResponse: AuthResponse) : AuthResult()

    @Serializable
    data class Message(val messageResponse: MessageResponse) : AuthResult()
}
