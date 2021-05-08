package de.plastickarma.kotlinx.serialization.format.ion.decode

import com.amazon.ion.system.IonSystemBuilder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
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
        error("decodeElementIndex not supported for IonDecoder")
    }

    private var afterNullCheck: Boolean = false

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            StructureKind.LIST -> {
                ion.stepIn()
                ContainerTypeDecoder(serializersModule, ion, this)
            }
            StructureKind.MAP -> {
                ion.stepIn()
                MapIonDecoder(serializersModule, ion, this)
            }
            else -> super.beginStructure(descriptor)
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        ion.stepOut()
    }

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        if (!afterNullCheck) {
            ion.next()
            afterNullCheck = false
        }
        return super.decodeSerializableValue(deserializer)
    }

    override fun decodeString(): String {
        return ion.stringValue()
    }

    override fun decodeInt(): Int {
        return ion.intValue()
    }

    override fun decodeFloat(): Float {
        return ion.doubleValue().toFloat()
    }

    override fun decodeDouble(): Double {
        return ion.doubleValue()
    }

    override fun decodeNull(): Nothing? {
        return null
    }

    override fun decodeNotNullMark(): Boolean {
        val isNull = ion.isNullValue
        afterNullCheck = true
        return !isNull
    }
}
