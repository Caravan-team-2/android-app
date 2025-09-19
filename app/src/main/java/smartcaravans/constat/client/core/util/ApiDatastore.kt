package smartcaravans.constat.client.core.util

import android.content.Context
import androidx.datastore.dataStore

val Context.apiDatastore by dataStore(
    fileName = "api-data",
    serializer = ApiDataSerializer
)