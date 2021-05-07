package de.plastickarma.kotlinx.serialization.format.ion

import com.amazon.ion.IonType
import com.amazon.ion.IonWriter
import com.amazon.ion.system.IonTextWriterBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import java.io.OutputStream

/**
 * Main entry point for serialization to ION format.
 */
@ExperimentalSerializationApi
class IonEncoder(outputStream: OutputStream) : AbstractEncoder() {

    private val textWriterBuilder: IonWriter = IonTextWriterBuilder
        .standard()
        .build(outputStream)

    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        when (descriptor.kind) {
            StructureKind.LIST -> textWriterBuilder.stepIn(IonType.LIST)
            StructureKind.CLASS -> {
                textWriterBuilder.stepIn(IonType.STRUCT)
                return ComplexTypeEncoder(serializersModule, textWriterBuilder, this)
            }
            StructureKind.MAP -> {
                textWriterBuilder.stepIn(IonType.STRUCT)
                return MapIonEncoder(serializersModule, textWriterBuilder, this)
            }
            else -> textWriterBuilder.stepIn(IonType.STRUCT)
        }
        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        textWriterBuilder.stepOut()
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        return true
    }

    override fun encodeBoolean(value: Boolean): Unit = textWriterBuilder.writeBool(value)
    override fun encodeByte(value: Byte): Unit = textWriterBuilder.writeInt(value.toLong())
    override fun encodeShort(value: Short): Unit = textWriterBuilder.writeInt(value.toLong())
    override fun encodeInt(value: Int): Unit = textWriterBuilder.writeInt(value.toLong())
    override fun encodeLong(value: Long): Unit = textWriterBuilder.writeInt(value)
    override fun encodeFloat(value: Float): Unit = textWriterBuilder.writeFloat(value.toDouble())
    override fun encodeDouble(value: Double): Unit = textWriterBuilder.writeFloat(value)
    override fun encodeChar(value: Char): Unit = textWriterBuilder.writeString(value.toString())
    override fun encodeString(value: String): Unit = textWriterBuilder.writeString(value)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = encodeString(enumDescriptor.getElementName(index))

    override fun encodeValue(value: Any) {
        textWriterBuilder.writeString(value.toString())
    }

    override fun encodeNull() {
        textWriterBuilder.writeNull()
    }
}
