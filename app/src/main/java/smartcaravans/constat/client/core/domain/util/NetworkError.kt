package smartcaravans.constat.client.core.domain.util

import kotlinx.serialization.Serializable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import smartcaravans.constat.client.R

@Serializable
enum class NetworkError(
    val code: Int,
    val titleRes: Int,
    val messageRes: Int,
    val icon: ImageVector,
):
    Error {
    // 4xx Client Error codes
    BAD_REQUEST(400, R.string.error_title_bad_request, R.string.error_bad_request, Icons.Default.ErrorOutline),
    UNAUTHORIZED(401, R.string.error_title_unauthorized, R.string.error_unauthorized, Icons.Default.Lock),
    PAYMENT_REQUIRED(402, R.string.error_title_payment_required, R.string.error_payment_required, Icons.Default.Payment),
    FORBIDDEN(403, R.string.error_title_forbidden, R.string.error_forbidden, Icons.Default.Block),
    NOT_FOUND(404, R.string.error_title_not_found, R.string.error_not_found, Icons.Default.Search),
    METHOD_NOT_ALLOWED(405, R.string.error_title_method_not_allowed, R.string.error_method_not_allowed, Icons.Default.DoNotDisturb),
    NOT_ACCEPTABLE(406, R.string.error_title_not_acceptable, R.string.error_not_acceptable, Icons.Default.ThumbDown),
    PROXY_AUTHENTICATION_REQUIRED(407, R.string.error_title_proxy_auth_required, R.string.error_proxy_auth_required, Icons.Default.Security),
    REQUEST_TIMEOUT(408, R.string.error_title_request_timeout, R.string.error_request_timeout, Icons.Default.AccessTime),
    CONFLICT(409, R.string.error_title_conflict, R.string.error_conflict, Icons.Default.Warning),
    GONE(410, R.string.error_title_gone, R.string.error_gone, Icons.Default.Delete),
    LENGTH_REQUIRED(411, R.string.error_title_length_required, R.string.error_length_required, Icons.Default.Straighten),
    PRECONDITION_FAILED(412, R.string.error_title_precondition_failed, R.string.error_precondition_failed, Icons.Default.Cancel),
    PAYLOAD_TOO_LARGE(413, R.string.error_title_payload_too_large, R.string.error_payload_too_large, Icons.Default.CloudUpload),
    URI_TOO_LONG(414, R.string.error_title_uri_too_long, R.string.error_uri_too_long, Icons.Default.Link),
    UNSUPPORTED_MEDIA_TYPE(415, R.string.error_title_unsupported_media_type, R.string.error_unsupported_media_type,
        Icons.AutoMirrored.Filled.InsertDriveFile
    ),
    RANGE_NOT_SATISFIABLE(416, R.string.error_title_range_not_satisfiable, R.string.error_range_not_satisfiable, Icons.Default.Tune),
    EXPECTATION_FAILED(417, R.string.error_title_expectation_failed, R.string.error_expectation_failed, Icons.Default.SentimentDissatisfied),
    IM_A_TEAPOT(418, R.string.error_title_im_a_teapot, R.string.error_im_a_teapot, Icons.Default.Coffee),
    MISDIRECTED_REQUEST(421, R.string.error_title_misdirected_request, R.string.error_misdirected_request, Icons.Default.DirectionsOff),
    UNPROCESSABLE_ENTITY(422, R.string.error_title_unprocessable_entity, R.string.error_unprocessable_entity, Icons.Default.BugReport),
    LOCKED(423, R.string.error_title_locked, R.string.error_locked, Icons.Default.LockPerson),
    FAILED_DEPENDENCY(424, R.string.error_title_failed_dependency, R.string.error_failed_dependency, Icons.Default.LinkOff),
    TOO_EARLY(425, R.string.error_title_too_early, R.string.error_too_early, Icons.Default.Schedule),
    UPGRADE_REQUIRED(426, R.string.error_title_upgrade_required, R.string.error_upgrade_required, Icons.Default.Update),
    PRECONDITION_REQUIRED(428, R.string.error_title_precondition_required, R.string.error_precondition_required,
        Icons.AutoMirrored.Filled.Rule
    ),
    TOO_MANY_REQUESTS(429, R.string.error_title_too_many_requests, R.string.error_too_many_requests, Icons.Default.Speed),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, R.string.error_title_request_header_too_large, R.string.error_request_header_too_large, Icons.Default.DataArray),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, R.string.error_title_unavailable_legal_reasons, R.string.error_unavailable_legal_reasons, Icons.Default.Gavel),

    // 5xx Server Error codes
    INTERNAL_SERVER_ERROR(500, R.string.error_title_internal_server_error, R.string.error_internal_server_error, Icons.Default.Dns),
    NOT_IMPLEMENTED(501, R.string.error_title_not_implemented, R.string.error_not_implemented, Icons.Default.BuildCircle),
    BAD_GATEWAY(502, R.string.error_title_bad_gateway, R.string.error_bad_gateway, Icons.Default.Router),
    SERVICE_UNAVAILABLE(503, R.string.error_title_service_unavailable, R.string.error_service_unavailable, Icons.Default.CloudOff),
    GATEWAY_TIMEOUT(504, R.string.error_title_gateway_timeout, R.string.error_gateway_timeout, Icons.Default.TimerOff),
    HTTP_VERSION_NOT_SUPPORTED(505, R.string.error_title_http_version_not_supported, R.string.error_http_version_not_supported, Icons.Default.Upgrade),
    VARIANT_ALSO_NEGOTIATES(506, R.string.error_title_variant_also_negotiates, R.string.error_variant_also_negotiates, Icons.Default.SwapHoriz),
    INSUFFICIENT_STORAGE(507, R.string.error_title_insufficient_storage, R.string.error_insufficient_storage, Icons.Default.Storage),
    LOOP_DETECTED(508, R.string.error_title_loop_detected, R.string.error_loop_detected, Icons.Default.Loop),
    NOT_EXTENDED(510, R.string.error_title_not_extended, R.string.error_not_extended, Icons.Default.ExtensionOff),
    NETWORK_AUTHENTICATION_REQUIRED(511, R.string.error_title_network_auth_required, R.string.error_network_auth_required, Icons.Default.NetworkCheck),

    // Custom network errors
    NO_INTERNET_CONNECTION(1000, R.string.error_title_no_internet_connection, R.string.no_internet_error, Icons.Default.WifiOff),
    REQUEST_CANCELLED(1001, R.string.error_title_request_cancelled, R.string.error_request_cancelled, Icons.Default.Cancel),
    SERIALIZATION_ERROR(1002, R.string.error_title_serialization_error, R.string.serialization_error, Icons.Default.Code),
    UNKNOWN_ERROR(1003, R.string.error_title_unknown_error, R.string.unknown_error, Icons.Default.QuestionMark);

    fun isClientError(): Boolean {
        return code in 400..499
    }
    fun isServerError(): Boolean {
        return code in 500..599
    }
    fun isConnectionError(): Boolean {
        return this == NO_INTERNET_CONNECTION || this == REQUEST_CANCELLED
    }

    companion object {
        fun fromCode(code: Int): NetworkError {
            return entries.find { it.code == code } ?: UNKNOWN_ERROR
        }


    }
}