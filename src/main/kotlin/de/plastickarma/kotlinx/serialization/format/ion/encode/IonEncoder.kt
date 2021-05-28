package de.plastickarma.kotlinx.serialization.format.ion.encode

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

    private val ion: IonWriter = IonTextWriterBuilder
        .standard()
        .build(outputStream)

    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return when (descriptor.kind) {
            StructureKind.LIST -> {
                ion.stepIn(IonType.LIST)
                this
            }
            StructureKind.CLASS -> {
                ion.stepIn(IonType.STRUCT)
                ComplexTypeEncoder(serializersModule, ion, this)
            }
            StructureKind.MAP -> {
                ion.stepIn(IonType.STRUCT)
                MapIonEncoder(serializersModule, ion, this)
            }
            else -> {
                ion.stepIn(IonType.STRUCT)
                this
            }
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        ion.stepOut()
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        return true
    }

    override fun encodeBoolean(value: Boolean): Unit = ion.writeBool(value)
    override fun encodeByte(value: Byte): Unit = ion.writeInt(value.toLong())
    override fun encodeShort(value: Short): Unit = ion.writeInt(value.toLong())
    override fun encodeInt(value: Int): Unit = ion.writeInt(value.toLong())
    override fun encodeLong(value: Long): Unit = ion.writeInt(value)
    override fun encodeFloat(value: Float): Unit = ion.writeFloat(value.toDouble())
    override fun encodeDouble(value: Double): Unit = ion.writeFloat(value)
    override fun encodeChar(value: Char): Unit = ion.writeString(value.toString())
    override fun encodeString(value: String): Unit = ion.writeString(value)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) =
        encodeString(enumDescriptor.getElementName(index))

    override fun encodeValue(value: Any) {
        ion.writeString(value.toString())
    }

    override fun encodeNull() {
        ion.writeNull()
    }
}
