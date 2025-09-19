package smartcaravans.constat.client.auth.domain.repo

import smartcaravans.constat.client.auth.domain.dto.AuthResponse
import smartcaravans.constat.client.auth.domain.dto.LoginRequest
import smartcaravans.constat.client.auth.domain.dto.SignupRequest
import smartcaravans.constat.client.core.domain.util.NetworkError
import smartcaravans.constat.client.core.domain.util.Result
import java.io.File

interface AuthRepo {
    suspend fun login(request: LoginRequest): Result<AuthResponse, NetworkError>
    suspend fun signup(request: SignupRequest): Result<AuthResponse, NetworkError>
    suspend fun uploadFile(file: File): Result<String, NetworkError>
    suspend fun logout()
}