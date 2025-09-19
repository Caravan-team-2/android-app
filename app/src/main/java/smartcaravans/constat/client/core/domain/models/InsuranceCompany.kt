package smartcaravans.constat.client.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class InsuranceCompany(
    val id: String = "",
    val companyName: String = "",
    val logoId: String = "",
    val integrationId: String = "",
)
