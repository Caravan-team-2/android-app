package smartcaravans.constat.client.core.util

import smartcaravans.constat.client.core.util.Resource.Error


sealed interface Resource<out D> {
    data object Idle: Resource<Nothing>
    data object Loading: Resource<Nothing>
    data class Success<out D>(val data: D): Resource<D>
    data class Error(val error: smartcaravans.constat.client.core.domain.util.Error): Resource<Nothing>
}

fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Idle -> Resource.Idle
        is Resource.Loading -> Resource.Loading
        is Resource.Success -> Resource.Success(transform(data))
        is Error -> Error(error)
    }
}