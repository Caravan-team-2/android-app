package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInsurance(
    val id: String,
    val carId: String,
    val vehicle: Vehicle,
    val insuranceNumber: String,
    val companyId: String,
    val validFrom: String,
    val validTo: String,
)
