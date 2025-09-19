package smartcaravans.constat.client.core.util

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import smartcaravans.constat.client.core.domain.models.User

class AccountManager(val dataStore: DataStore<ApiData>) {
    private val _user = dataStore.data.map { it.user }
    val user = _user

    suspend fun getUser() = dataStore.data.map { it.user }.first()

    suspend fun update(newUser: User?) = dataStore.updateData {
        it.copy(user = newUser)
    }

    suspend fun logout() {
        dataStore.updateData { it.copy(accessToken = "", refreshToken = "", user = null) }
    }
}