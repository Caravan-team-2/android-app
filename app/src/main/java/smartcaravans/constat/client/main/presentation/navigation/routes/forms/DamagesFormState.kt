package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import smartcaravans.constat.client.core.domain.models.InsuranceCompany
import smartcaravans.constat.client.core.presentation.util.InputError
import java.time.LocalDateTime

data class DamagesFormState(
    val selected: List<DamagePlaces> = emptyList(),
)
