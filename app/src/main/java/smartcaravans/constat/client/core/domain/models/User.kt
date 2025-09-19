package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val role: String = "",
    val kycId: String? = null,
    val insuranceCompanyId: String? = null,
    val isKycVerified: Boolean = false,
    val job: String = "",
    val isMailVerified: Boolean = false,
    val createdAt: String = "",
    val updatedAt: String = ""
) {
    fun getQrCodeData(): String {
        return "$id"
    }
}
