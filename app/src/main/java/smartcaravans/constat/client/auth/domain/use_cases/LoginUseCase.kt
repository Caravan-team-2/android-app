package smartcaravans.constat.client.auth.domain.use_cases

import smartcaravans.constat.client.auth.domain.dto.LoginRequest
import smartcaravans.constat.client.auth.domain.repo.AuthRepo

class LoginUseCase(val repository: AuthRepo) {
    suspend operator fun invoke(request: LoginRequest) = repository.login(request)
}