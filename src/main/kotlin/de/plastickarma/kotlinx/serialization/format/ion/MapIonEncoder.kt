package de.plastickarma.kotlinx.serialization.format.ion

import com.amazon.ion.IonWriter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

/**
 * Specialized encoder to write a map in ION format.
 */
@ExperimentalSerializationApi
class MapIonEncoder(
    override val serializersModule: SerializersModule,
    private val ion: IonWriter,
    private val baseEncoder: Encoder
) : AbstractEncoder() {

    private var isKey: Boolean = true

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        isKey = if (isKey) {
            ion.setFieldName(value.toString())
            false
        } else {
            serializer.serialize(baseEncoder, value)
            true
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        ion.stepOut()
    }
}
