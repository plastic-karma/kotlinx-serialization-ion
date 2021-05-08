package de.plastickarma.kotlinx.serialization.format.ion.decode

import com.amazon.ion.IonReader
import com.amazon.ion.IonType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class ContainerTypeDecoder(
    override val serializersModule: SerializersModule,
    private val ion: IonReader,
    private val base: AbstractDecoder
) : CompositeDecoder by base, Decoder by base {

    private var elementIndex: Int = 0
    private var next: IonType? = null

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        next = ion.next()
        return if (next == null) {
            CompositeDecoder.DECODE_DONE
        } else {
            elementIndex++
        }
    }

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        return deserializer.deserialize(this)
    }
}
