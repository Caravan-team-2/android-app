package smartcaravans.constat.client.main.domain.repo

import smartcaravans.constat.client.GetConstatsQuery
import smartcaravans.constat.client.UserInsurrancesQuery
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.domain.util.SimpleError

interface MainRepo {
    suspend fun getConstats(): Result<List<GetConstatsQuery.Data1>, SimpleError>
    suspend fun getInsurrances(): Result<List<UserInsurrancesQuery.UserInsurrance>, SimpleError>
}