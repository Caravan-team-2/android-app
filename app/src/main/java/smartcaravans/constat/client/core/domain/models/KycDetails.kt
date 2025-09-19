package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KycDetails(
    val id: String = "",
    val nin: String = "",
    val firstName: String = "",
    val lastName: String = "",
    @SerialName("dob")
    val dateOfBirth: String = "",
    val sex: Sex = Sex.MALE,
    val placeOfBirth: String = "",
    val issuedAt: String = "",
    val expiresAt: String = "",
    val licenseType: LicenseType = LicenseType.A,
    val licenseNumber: String = "",
)
