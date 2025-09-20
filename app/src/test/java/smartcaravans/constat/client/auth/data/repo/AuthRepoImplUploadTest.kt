package smartcaravans.constat.client.auth.data.repo

import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.kotlin.mock
import smartcaravans.constat.client.core.data.networking.ApiCall
import smartcaravans.constat.client.core.domain.models.User
import smartcaravans.constat.client.core.domain.util.NetworkError
import smartcaravans.constat.client.core.domain.util.Result
import smartcaravans.constat.client.core.util.AccountManager
import smartcaravans.constat.client.core.util.ApiData
import java.io.File

class AuthRepoImplUploadTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockAccountManager: MockAccountManager
    private lateinit var authRepo: AuthRepoImpl
    private lateinit var testFile: File

    @Before
    fun setUp() {
        mockAccountManager = MockAccountManager()

        // Create a test file with realistic image data
        testFile = tempFolder.newFile("test_image.jpg")
        testFile.writeBytes(byteArrayOf(
            0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte(), // JPEG header
            0x00, 0x10, 0x4A, 0x46, 0x49, 0x46 // JFIF marker
        ))
    }

    @Test
    fun `uploadFile with valid file returns success with file URL`() = runTest {
        // Arrange
        val expectedUrl = "https://example.com/uploads/test_image.jpg"
        val mockEngine = MockEngine { request ->
            // Verify HTTP method and endpoint
            assertThat(request.method.value).isEqualTo("POST")
            assertThat(request.url.encodedPath).contains("upload")

            // Verify multipart content type
            val contentType = request.headers[HttpHeaders.ContentType]
            assertThat(contentType).contains("multipart/form-data")

            respond(
                content = expectedUrl,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data).isEqualTo(expectedUrl)
    }

    @Test
    fun `uploadFile with empty file succeeds`() = runTest {
        // Arrange
        val emptyFile = tempFolder.newFile("empty.txt")
        val expectedUrl = "https://example.com/uploads/empty.txt"

        val mockEngine = MockEngine {
            respond(
                content = expectedUrl,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(emptyFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedUrl)
    }

    @Test
    fun `uploadFile with large file succeeds`() = runTest {
        // Arrange
        val largeFile = tempFolder.newFile("large_file.bin")
        val largeData = ByteArray(2 * 1024 * 1024) { (it % 256).toByte() } // 2MB file
        largeFile.writeBytes(largeData)

        val expectedUrl = "https://example.com/uploads/large_file.bin"
        val mockEngine = MockEngine { request ->
            // Verify the file size is properly handled
            assertThat(request.body).isNotNull()

            respond(
                content = expectedUrl,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(largeFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedUrl)
    }

    @Test
    fun `uploadFile with special characters in filename succeeds`() = runTest {
        // Arrange
        val specialFile = tempFolder.newFile("test file with spaces & symbols (1).jpg")
        specialFile.writeBytes(byteArrayOf(1, 2, 3, 4, 5))

        val expectedUrl = "https://example.com/uploads/encoded_filename.jpg"
        val mockEngine = MockEngine { request ->
            // Verify that special characters are handled in the request
            assertThat(request.headers.get(HttpHeaders.ContentDisposition)).isNotNull()

            respond(
                content = expectedUrl,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(specialFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedUrl)
    }

    @Test
    fun `uploadFile with server error returns error result`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.error).isInstanceOf(NetworkError::class.java)
    }

    @Test
    fun `uploadFile with unauthorized error returns unauthorized`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            respond(
                content = "Unauthorized",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.error).isEqualTo(NetworkError.UNAUTHORIZED)
    }

    @Test
    fun `uploadFile with bad request returns bad request error`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            respond(
                content = "Bad Request - File too large",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.error).isEqualTo(NetworkError.BAD_REQUEST)
    }

    @Test
    fun `uploadFile with timeout returns timeout error`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            respond(
                content = "Request Timeout",
                status = HttpStatusCode.RequestTimeout,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.error).isEqualTo(NetworkError.REQUEST_TIMEOUT)
    }

    @Test
    fun `uploadFile with forbidden returns forbidden error`() = runTest {
        // Arrange
        val mockEngine = MockEngine {
            respond(
                content = "Forbidden",
                status = HttpStatusCode.Forbidden,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.error).isEqualTo(NetworkError.FORBIDDEN)
    }

    @Test
    fun `uploadFile with nonexistent file throws exception`() = runTest {
        // Arrange
        val nonexistentFile = File(tempFolder.root, "nonexistent.txt")

        val mockEngine = MockEngine { respond("", HttpStatusCode.OK) }
        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act & Assert
        try {
            authRepo.uploadFile(nonexistentFile)
            assertThat(false).isTrue()
        } catch (e: Exception) {
            assertThat(e).isAnyOf(
                java.io.FileNotFoundException::class.java,
                IllegalArgumentException::class.java
            )
        }
    }

    @Test
    fun `uploadFile verifies request contains file data`() = runTest {
        // Arrange
        var requestBodyVerified = false
        val mockEngine = MockEngine { request ->
            // Verify that the request body contains multipart data
            assertThat(request.body).isNotNull()
            assertThat(request.headers[HttpHeaders.ContentType]).contains("multipart")
            requestBodyVerified = true

            respond(
                content = "https://example.com/uploads/test.jpg",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = createMockHttpClient(mockEngine)
        val apiCall = ApiCall(httpClient)
        authRepo = AuthRepoImpl(mockAccountManager, apiCall)

        // Act
        val result = authRepo.uploadFile(testFile)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(requestBodyVerified).isTrue()
    }

    @Test
    fun `uploadFile with different file types succeeds`() = runTest {
        // Test multiple file types
        val testCases = listOf(
            Pair("document.pdf", byteArrayOf(0x25, 0x50, 0x44, 0x46)), // PDF header
            Pair("image.png", byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47)), // PNG header
            Pair("text.txt", "Hello World".toByteArray())
        )

        testCases.forEach { (filename, data) ->
            // Arrange
            val file = tempFolder.newFile(filename)
            file.writeBytes(data)

            val expectedUrl = "https://example.com/uploads/$filename"
            val mockEngine = MockEngine {
                respond(
                    content = expectedUrl,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "text/plain")
                )
            }

            val httpClient = createMockHttpClient(mockEngine)
            val apiCall = ApiCall(httpClient)
            val repo = AuthRepoImpl(mockAccountManager, apiCall)

            // Act
            val result = repo.uploadFile(file)

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(expectedUrl)
        }
    }

    private fun createMockHttpClient(mockEngine: MockEngine): HttpClient {
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    // Mock AccountManager for testing
    private class MockAccountManager : AccountManager(mock()) {
        override val user = MutableStateFlow<User?>(null)

        override suspend fun update(newUser: User?): ApiData {
            user.value = newUser
            return mock()
        }
    }
}
