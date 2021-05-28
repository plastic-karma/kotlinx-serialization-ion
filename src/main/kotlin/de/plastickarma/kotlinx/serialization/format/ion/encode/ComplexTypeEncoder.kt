package de.plastickarma.kotlinx.serialization.format.ion.encode

import com.amazon.ion.IonWriter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

/**
 * Encoder for complex types like classes.
 */
@ExperimentalSerializationApi
class ComplexTypeEncoder(
    override val serializersModule: SerializersModule,
    private val ion: IonWriter,
    private val baseEncoder: Encoder
) : CompositeEncoder {

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeBoolean(value)
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeByte(value)
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeChar(value)
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeDouble(value)
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeFloat(value)
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeInt(value)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeLong(value)
    }
    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeShort(value)
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeString(value)
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        return baseEncoder
    }

    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        if (value != null) {
            encodeSerializableElement(descriptor, index, serializer, value)
        } else {
            ion.setFieldName(descriptor.getElementName(index))
            ion.writeNull()
        }
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        ion.setFieldName(descriptor.getElementName(index))
        baseEncoder.encodeSerializableValue(serializer, value)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        ion.stepOut()
    }
}
