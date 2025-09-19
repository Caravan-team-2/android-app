package smartcaravans.constat.client.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Converts a date string returned by Laravel API to LocalDateTime object.
 * Handles various Laravel date formats including:
 * - ISO 8601 with microseconds: "2023-12-25T10:30:45.123456Z"
 * - ISO 8601 with milliseconds: "2023-12-25T10:30:45.123Z"
 * - ISO 8601 without fractional seconds: "2023-12-25T10:30:45Z"
 * - Laravel's default format: "2023-12-25 10:30:45"
 */
fun String.toLocalDateTime(): LocalDateTime? {
    if (this.isBlank()) return null

    // List of common Laravel date formats
    val formatters = listOf(
        // ISO 8601 formats (typical for API responses)
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"),
        // Laravel's default database format
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        // Additional common formats
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )

    // Try each formatter until one works
    for (formatter in formatters) {
        try {
            // Remove 'Z' suffix for local datetime parsing (except for timezone-aware formats)
            val dateString = if (this.endsWith("Z") && !formatter.toString().contains("XXX")) {
                this.dropLast(1)
            } else {
                this
            }

            return LocalDateTime.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            // Continue to next formatter
            continue
        }
    }

    // If all formatters fail, return null
    return null
}
