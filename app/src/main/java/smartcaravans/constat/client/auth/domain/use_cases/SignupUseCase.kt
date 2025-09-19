package smartcaravans.constat.client.auth.domain.use_cases

import smartcaravans.constat.client.auth.domain.dto.LoginRequest
import smartcaravans.constat.client.auth.domain.dto.SignupRequest
import smartcaravans.constat.client.auth.domain.repo.AuthRepo

class SignupUseCase(val repository: AuthRepo) {
    suspend operator fun invoke(request: SignupRequest) = repository.signup(request)
}