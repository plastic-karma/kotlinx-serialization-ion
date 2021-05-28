package de.plastickarma.kotlinx.serialization.format.ion

import de.plastickarma.kotlinx.serialization.format.ion.decode.IonDecoder
import de.plastickarma.kotlinx.serialization.format.ion.encode.IonEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.serializer
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream

/**
 * Main entry point into ION serialization/deserialization.
 */
@ExperimentalSerializationApi
class Ion private constructor() {

    companion object {

        /**
         * Writes the given value into an ION string.
         */
        inline fun <reified T> encodeToString(value: T): String {
            val outputBuffer = ByteArrayOutputStream()
            BufferedOutputStream(outputBuffer).use {
                val encoder = IonEncoder(it)
                encoder.encodeSerializableValue(serializer(), value)
            }
            return outputBuffer.toString(Charsets.UTF_8.name())
        }

        /**
         * Reads the given ION string into a Kotlin object
         */
        inline fun <reified T> decodeFromString(value: String): T {
            val decoder = IonDecoder(value)
            return decoder.decodeSerializableValue(serializer())
        }
    }
}
