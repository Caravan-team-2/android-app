package smartcaravans.constat.client.core.util

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Query
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.domain.util.SimpleError

suspend fun <D: Query.Data, R> ApolloClient.myQuery(
    query: Query<D>,
    map: (D) -> R
): Result<R, SimpleError> =
    query(query).execute().let { response ->
        response.data?.let { data ->
            Result.Success(map(data))
        } ?: run {
            val errorMessage = response.errors?.firstOrNull()?.message ?: "Unknown error"
            Result.Error(SimpleError(errorMessage))
        }
    }

suspend fun <D: Query.Data> ApolloClient.myQuery(
    query: Query<D>,
): Result<D, SimpleError> = myQuery(query) { it }