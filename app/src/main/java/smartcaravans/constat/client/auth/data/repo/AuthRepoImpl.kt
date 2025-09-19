package smartcaravans.constat.client.auth.data.repo

import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import smartcaravans.constat.client.auth.domain.dto.AuthResponse
import smartcaravans.constat.client.auth.domain.dto.LoginRequest
import smartcaravans.constat.client.auth.domain.dto.MessageResponse
import smartcaravans.constat.client.auth.domain.dto.SignupRequest
import smartcaravans.constat.client.auth.domain.repo.AuthRepo
import smartcaravans.constat.client.core.data.networking.ApiCall
import smartcaravans.constat.client.core.data.networking.safeCall
import smartcaravans.constat.client.core.data.networking.toUrl
import smartcaravans.constat.client.core.domain.util.NetworkError
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.util.AccountManager
import smartcaravans.constat.client.core.util.ApiRoutes
import java.io.File

class AuthRepoImpl(
    val accountManager: AccountManager,
    val apiCall: ApiCall,
): AuthRepo {

    private suspend fun handleAuthResponse(
        apiCall: suspend () -> Result<String, NetworkError>
    ): Result<AuthResponse, NetworkError> {
        return when (val result = apiCall()) {
            is Result.Success -> {
                try {
                    // Try to parse as AuthResponse first
                    val authResponse = Json.decodeFromString<AuthResponse>(result.data)
                    accountManager.update(authResponse.user)
                    Result.Success(authResponse)
                } catch (e: Exception) {
                    try {
                        // If that fails, try to parse as MessageResponse
                        val messageResponse = Json.decodeFromString<MessageResponse>(result.data)
                        // Return as an error with the message
                        Result.Error(NetworkError.UNKNOWN_ERROR) // You might want to create a custom error type
                    } catch (e: Exception) {
                        Result.Error(NetworkError.SERIALIZATION_ERROR)
                    }
                }
            }
            is Result.Error -> result
        }
    }

    override suspend fun login(request: LoginRequest): Result<AuthResponse, NetworkError> =
        handleAuthResponse {
            apiCall<String>(
                ApiRoutes.LOGIN,
                body = request
            )
        }

    override suspend fun signup(request: SignupRequest): Result<AuthResponse, NetworkError> =
        handleAuthResponse {
            apiCall<String>(
                ApiRoutes.SIGNUP,
                body = request
            )
        }

    override suspend fun uploadFile(file: File): Result<String, NetworkError> {
        return safeCall {
            apiCall.client.submitFormWithBinaryData(
                url = ApiRoutes.UPLOAD_FILE.toUrl(),
                formData = formData {
                    append("file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "application/octet-stream")
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                    })
                }
            )
        }
    }

    override suspend fun logout() {

    }
}