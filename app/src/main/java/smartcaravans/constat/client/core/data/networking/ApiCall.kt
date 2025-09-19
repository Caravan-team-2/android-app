package smartcaravans.constat.client.core.data.networking

import smartcaravans.constat.client.core.domain.util.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.util.ApiRouteType
import smartcaravans.constat.client.core.util.ApiRoutes

class ApiCall(val client: HttpClient) {
    suspend inline operator fun <reified T> invoke(
        routes: ApiRoutes,
        params: Map<String, String> = emptyMap(),
        body: Any? = null
    ): Result<T, NetworkError> {
        println("ApiCall: $routes, params: $params, body: $body")
        val block: HttpRequestBuilder.() -> Unit = {
            body?.let {
                setBody(body)
            }
        }
        return safeCall {
            when (routes.type) {
                ApiRouteType.GET -> client.get(routes.toUrl(params), block)
                ApiRouteType.POST -> client.post(routes.toUrl(params), block)
                ApiRouteType.PATCH -> client.patch(routes.toUrl(params), block)
                ApiRouteType.DELETE -> client.delete(routes.toUrl(params), block)
            }
        }
    }
}