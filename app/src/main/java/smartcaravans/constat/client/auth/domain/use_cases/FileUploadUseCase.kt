package smartcaravans.constat.client.auth.domain.use_cases

import smartcaravans.constat.client.auth.domain.repo.AuthRepo
import java.io.File

class FileUploadUseCase(val repo: AuthRepo) {
    suspend operator fun invoke(file: File) = repo.uploadFile(file)
}