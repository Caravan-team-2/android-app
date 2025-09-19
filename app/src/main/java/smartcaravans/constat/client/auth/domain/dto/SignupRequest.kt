package smartcaravans.constat.client.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val firstName: String,
    val lastName: String,
    val insuranceNumber: String,
    val phoneNumber: String,
    val job: String,
    val email: String,
    val password: String,
)
