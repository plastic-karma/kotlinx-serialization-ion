package de.plastickarma.kotlinx.serialization.format.ion.decode

import com.amazon.ion.IonReader
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

/**
 * Decoder for Maps.
 */
@ExperimentalSerializationApi
class MapIonDecoder(
    override val serializersModule: SerializersModule,
    private val ion: IonReader,
    private val base: AbstractDecoder
) : AbstractDecoder() {

    private var elementIndex: Int = 0
    private var isKey: Boolean = true

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        // ION maps have one index for a key/value pair, where kotlinx-serialization assumes new index for
        // every new key and value. Thus, we are only advancing ION reader, when we are at a new key position.
        return if (isKey && ion.next() == null) {
            CompositeDecoder.DECODE_DONE
        } else {
            elementIndex++
        }
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        isKey = true
        return base.beginStructure(descriptor)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        ion.stepOut()
    }

    private inline fun <T> keyAwareValue(
        keyExtractor: (String) -> T,
        valueExtractor: () -> T
    ): T {
        return if (isKey) {
            isKey = false
            keyExtractor(ion.fieldName)
        } else {
            isKey = true
            valueExtractor()
        }
    }

    override fun decodeInt(): Int = keyAwareValue(String::toInt) { ion.intValue() }
    override fun decodeString(): String = keyAwareValue({ it }) { ion.stringValue() }
}
