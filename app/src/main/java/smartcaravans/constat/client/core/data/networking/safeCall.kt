package smartcaravans.constat.client.core.data.networking

import smartcaravans.constat.client.core.domain.util.NetworkError
import smartcaravans.constat.client.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.io.EOFException
import java.net.ConnectException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(call: () -> HttpResponse): Result<T, NetworkError> {
    val response = try {
        call()
    } catch (_: ConnectException) {
        return Result.Error(NetworkError.NO_INTERNET_CONNECTION)
    } catch (_: EOFException) {
        return Result.Error(NetworkError.NO_INTERNET_CONNECTION)
    } catch (_: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET_CONNECTION)
    } catch (_: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    } catch (_: JsonConvertException) {
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN_ERROR)
    }
    return responseToResult(response)
}