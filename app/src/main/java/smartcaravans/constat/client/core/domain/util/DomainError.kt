package smartcaravans.constat.client.core.domain.util

import kotlinx.serialization.Serializable
import smartcaravans.constat.client.R

@Serializable
enum class DomainError(val text: Int, val description: Int? = null): Error {
    SERVER_ERROR(R.string.server_error),
    INTERNAL_ERROR(R.string.internal_error),
    USER_ALREADY_EXISTS(R.string.user_exists_error),
    INCORRECT_CREDENTIALS(R.string.incorrect_credentials_error),
    USER_NOT_FOUND(R.string.user_not_found_error),
    SERIALIZATION_ERROR(R.string.serialization_error),
    UNKNOWN(R.string.unknown_error),
    NO_INTERNET(R.string.no_internet_error),
    EMAIL_ERROR(R.string.email_not_verified_error),
    INCORRECT_VERIFICATION_CODE(R.string.incorrect_code),
    TIMEOUT(R.string.timeout),
}