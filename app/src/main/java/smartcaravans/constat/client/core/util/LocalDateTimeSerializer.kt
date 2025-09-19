package smartcaravans.constat.client.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Custom serializer for LocalDateTime that handles Laravel API date formats
 * Automatically converts between Laravel date strings and LocalDateTime objects
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    /**
     * Deserialize Laravel date string to LocalDateTime
     */
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateString = decoder.decodeString()
        return dateString.toLocalDateTime()
            ?: throw IllegalArgumentException("Cannot parse date: $dateString")
    }

    /**
     * Serialize LocalDateTime to Laravel API format (ISO 8601)
     */
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        encoder.encodeString(value.format(formatter))
    }
}
