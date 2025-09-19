package smartcaravans.constat.client.di

import androidx.datastore.core.DataStore
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import smartcaravans.constat.client.core.util.apiDatastore
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import smartcaravans.constat.client.BuildConfig
import smartcaravans.constat.client.core.data.networking.ApiCall
import smartcaravans.constat.client.core.util.ApiData
import smartcaravans.constat.client.core.util.LocalDateTimeSerializer
import smartcaravans.constat.client.main.data.MainRepoImpl
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

val networkModule = module {
    single(StringQualifier("api-datastore")) {
        androidContext().apiDatastore
    }
    single(StringQualifier("default-config")) {
        val config: HttpClientConfig<*>.() -> Unit = {
            defaultRequest {
                header(HttpHeaders.Accept, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("HttpClient").i(message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    encodeDefaults = true
                    explicitNulls = false
                    serializersModule = SerializersModule {
                        contextual(LocalDateTime::class, LocalDateTimeSerializer)
                    }
                })
            }
        }
        config
    }

    single(StringQualifier("default-client")) {
        val defaultConfig =
            get<HttpClientConfig<*>.() -> Unit>(StringQualifier("default-config"))
        HttpClient {
            defaultConfig()
        }
    }

    single(StringQualifier("my-api-client")) {
        val dataStore = get<DataStore<ApiData>>(StringQualifier("api-datastore"))
        val defaultConfig =
            get<HttpClientConfig<*>.() -> Unit>(StringQualifier("default-config"))
        HttpClient {
            defaultConfig()
            install(Auth) {
                bearer {
                    loadTokens {
                        // Dynamically load token from datastore for each request
                        dataStore.data.first()
                            .takeIf {
                                it.accessToken.isNotBlank() and it.refreshToken.isNotBlank()
                            }
                            ?.let {
                                BearerTokens(it.accessToken, it.refreshToken)
                            }
                    }
                    refreshTokens {
                        // Refresh logic if needed - for now just reload from datastore
                        dataStore.data.first()
                            .takeIf {
                                it.accessToken.isNotBlank() and it.refreshToken.isNotBlank()
                            }
                            ?.let {
                                BearerTokens(it.accessToken, it.refreshToken)
                            }
                    }
                }
            }
            install(ResponseObserver) {
                onResponse { response ->
                    if (response.status.value in 200..299) {
                        response.bodyAsText().let { body ->
                            val obj = Json.parseToJsonElement(body).jsonObject
                            val accessToken = obj["accessToken"]?.jsonPrimitive?.content
                            val refreshToken = obj["refreshToken"]?.jsonPrimitive?.content
                            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank())
                                runBlocking {
                                    dataStore.updateData {
                                        it.copy(
                                            accessToken = accessToken,
                                            refreshToken = refreshToken
                                        )
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    single(StringQualifier("my-api-call")) {
        ApiCall(get(StringQualifier("my-api-client")))
    }

    single(StringQualifier("default-call")) {
        ApiCall(get(StringQualifier("default-client")))
    }

    single {
        val dataStore = get<DataStore<ApiData>>(StringQualifier("api-datastore"))
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val authInterceptor = Interceptor { chain ->
            val request = chain.request()
            val token = runBlocking { dataStore.data.first().accessToken }
            val newRequest = if (token.isNotBlank()) {
                request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                request
            }
            chain.proceed(newRequest)
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        ApolloClient.Builder()
            .serverUrl("${BuildConfig.BASE_URL}graphql/")
            .okHttpClient(get<OkHttpClient>())
            .build()
    }

    single {
        MainRepoImpl(get())
    }
}