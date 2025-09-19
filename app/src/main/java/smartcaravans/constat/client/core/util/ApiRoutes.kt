package smartcaravans.constat.client.core.util

enum class ApiRoutes(
    val endpoint: String,
    val type: ApiRouteType = ApiRouteType.GET,
    val baseUrls: BaseUrls = BaseUrls.MY_API
) {
    LOGIN("authentication/login", ApiRouteType.POST),
    SIGNUP("authentication/register", ApiRouteType.POST),
    REFRESH("refresh", ApiRouteType.POST),
    USER("user"),
    UPLOAD_FILE("upload", ApiRouteType.POST),
}