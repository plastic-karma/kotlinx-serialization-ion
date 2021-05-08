package de.plastickarma.kotlinx.serialization.format.ion.decode

import com.amazon.ion.system.IonSystemBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * Main entry point for deserialization from ION format.
 */
@ExperimentalSerializationApi
class IonDecoder(value: String) : AbstractDecoder() {

    private val ion = IonSystemBuilder.standard().build().newReader(value)

    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        TODO("Not yet implemented")
    }

    override fun decodeString(): String {
        ion.next()
        return ion.stringValue()
    }

    override fun decodeInt(): Int {
        ion.next()
        return ion.intValue()
    }

    override fun decodeFloat(): Float {
        ion.next()
        return ion.doubleValue().toFloat()
    }

    override fun decodeDouble(): Double {
        ion.next()
        return ion.doubleValue()
    }

    override fun decodeNull(): Nothing? {
        return null
    }

    override fun decodeNotNullMark(): Boolean {
        ion.next()
        return !ion.isNullValue
    }
}
