package smartcaravans.constat.client.main.domain.use_case

import smartcaravans.constat.client.main.domain.repo.MainRepo

class GetInsurrancesUseCase(val repo: MainRepo) {
    suspend operator fun invoke() = repo.getInsurrances()
}