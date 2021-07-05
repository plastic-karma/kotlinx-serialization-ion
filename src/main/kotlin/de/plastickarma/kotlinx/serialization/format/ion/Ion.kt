package de.plastickarma.kotlinx.serialization.format.ion

import com.amazon.ion.IonSystem
import com.amazon.ion.system.IonBinaryWriterBuilder
import com.amazon.ion.system.IonSystemBuilder
import com.amazon.ion.system.IonTextWriterBuilder
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
        inline fun <reified T> encodeToString(
            value: T,
            ionTextWriterBuilder: IonTextWriterBuilder = IonTextWriterBuilder.standard()
        ): String {
            val outputBuffer = ByteArrayOutputStream()
            BufferedOutputStream(outputBuffer).use {
                val encoder = IonEncoder(
                    ionTextWriterBuilder.build(outputBuffer)
                )
                encoder.encodeSerializableValue(serializer(), value)
            }
            return outputBuffer.toString(Charsets.UTF_8.name())
        }

        /**
         * Writes the given value into binary ION
         */
        inline fun <reified T> encodeToBytes(value: T): ByteArray {
            val outputBuffer = ByteArrayOutputStream()
            BufferedOutputStream(outputBuffer).use {
                IonBinaryWriterBuilder.standard().build(it).use { ion ->
                    IonEncoder(ion).encodeSerializableValue(serializer(), value)
                }
            }
            return outputBuffer.toByteArray()
        }

        /**
         * Reads the given ION string into a Kotlin object
         */
        inline fun <reified T> decodeFromBytes(
            value: ByteArray,
            ionSystem: IonSystem = IonSystemBuilder.standard().build()
        ): T {
            return ionSystem.newReader(value).use { ion ->
                IonDecoder(ion).decodeSerializableValue(serializer())
            }
        }

        /**
         * Reads the given ION string into a Kotlin object
         */
        inline fun <reified T> decodeFromString(value: String): T {
            val decoder = IonDecoder(IonSystemBuilder.standard().build().newReader(value))
            return decoder.decodeSerializableValue(serializer())
        }
    }
}
