package smartcaravans.constat.client.main.util

import smartcaravans.constat.client.UserInsurrancesQuery

fun List<UserInsurrancesQuery.UserInsurrance>.mapToVehicles(): List<UserInsurrancesQuery.Vehicle> {
    return map { it.vehicle }
}