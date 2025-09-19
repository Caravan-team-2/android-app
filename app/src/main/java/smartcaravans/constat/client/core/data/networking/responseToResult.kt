package smartcaravans.constat.client.core.data.networking

import smartcaravans.constat.client.core.domain.util.NetworkError
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.domain.util.Result.*
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                Error(NetworkError.SERIALIZATION_ERROR)
            }
        }
        else -> Error(NetworkError.fromCode(response.status.value))
    }
}
