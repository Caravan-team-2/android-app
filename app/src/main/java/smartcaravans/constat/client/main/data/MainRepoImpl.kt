package smartcaravans.constat.client.main.data

import com.apollographql.apollo.ApolloClient
import smartcaravans.constat.client.GetConstatsQuery
import smartcaravans.constat.client.UserInsurrancesQuery
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.domain.util.SimpleError
import smartcaravans.constat.client.core.util.myQuery
import smartcaravans.constat.client.main.domain.repo.MainRepo

class MainRepoImpl(
    val client: ApolloClient
): MainRepo {
    override suspend fun getConstats(): Result<List<GetConstatsQuery.Data1>, SimpleError> =
        client.myQuery(GetConstatsQuery()) {
            it.getConstats.data
        }

    override suspend fun getInsurrances(): Result<List<UserInsurrancesQuery.UserInsurrance>, SimpleError> =
        client.myQuery(UserInsurrancesQuery()) {
            it.userInsurrances
        }
}