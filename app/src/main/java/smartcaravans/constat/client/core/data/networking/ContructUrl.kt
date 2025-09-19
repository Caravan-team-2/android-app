package smartcaravans.constat.client.core.data.networking

import smartcaravans.constat.client.core.util.ApiRoutes

fun ApiRoutes.toUrl(params: Map<String, String> = emptyMap()): String {
    val baseUrl = baseUrls.url.dropLastWhile { it == '/' }
    var endpoint = endpoint.dropWhile { it == '/' }
    params.forEach { param ->
        endpoint = endpoint.replace("{${param.key}}", param.value)
    }
    return "$baseUrl/$endpoint"
}