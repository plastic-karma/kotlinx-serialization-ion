package de.plastickarma.kotlinx.serialization.format.ion.binary

import de.plastickarma.kotlinx.serialization.format.ion.Ion
import de.plastickarma.kotlinx.serialization.format.ion.data.ListElement
import de.plastickarma.kotlinx.serialization.format.ion.data.NestedDataHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.PrimitiveDataHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.Status
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class BinaryIonTest : FreeSpec({

    "complex values are getting serialized and deserialized in binary format" {
        val input = PrimitiveDataHolder(
            str = "Hello",
            'c',
            1,
            100000000001,
            1234.53f,
            -0.12312313,
            12,
            100,
            true,
            Status.UPDATED
        )

        val encoded = Ion.encodeToBytes(input)
        val output = Ion.decodeFromBytes<PrimitiveDataHolder>(encoded)

        output shouldBe input
    }

    "nested complex values are getting serialized and deserialized in binary format" {
        val input = NestedDataHolder(
            primitiveDataHolder = PrimitiveDataHolder(
                str = "Hello", 'c', 1, 100000000001, 1234.53f,
                -0.12312313, 12, 100, true, status = Status.UPDATED
            ),
            listOfPrimitiveDataHolder = listOf(
                PrimitiveDataHolder(
                    str = "Hello1", 'c', 1, 100000000001, 1234.51f,
                    -0.12312313, 12, 100, true, status = Status.UPDATED
                ),
                PrimitiveDataHolder(
                    str = "Hello2", 'c', 2, 200000000001, 1234.52f,
                    -0.22312313, 22, 101, false, status = Status.DELETED
                ),
                PrimitiveDataHolder(
                    str = "Hello3", 'c', 3, 300000000001, 1234.53f,
                    -0.32312313, 32, 102, true, status = Status.UPDATED
                ),
            ),
            mapOfPrimitiveDataHolder = mapOf(
                "key1" to PrimitiveDataHolder(
                    str = "HelloKey1", 'c', 1, 100000000001, 1234.51f,
                    -0.12312313, 12, 100, true, status = Status.UPDATED
                ),
                "key2" to PrimitiveDataHolder(
                    str = "HelloKey2", 'c', 1, 100000000001, 1234.51f,
                    -0.12312313, 12, 100, true, status = Status.DELETED
                ),
            )
        )

        val encoded = Ion.encodeToBytes(input)
        val output = Ion.decodeFromBytes<NestedDataHolder>(encoded)

        output shouldBe input
    }

    "maps are serialized and deserialized in binary format" {
        Ion.decodeFromBytes<Map<String, String>>(
            Ion.encodeToBytes(emptyMap<String, String>())
        ) shouldBe emptyMap<String, String>()

        Ion.decodeFromBytes<Map<String, Int>>(
            Ion.encodeToBytes(
                mapOf(
                    "A" to 1,
                    "B" to 2
                )
            )
        ) shouldBe mapOf(
            "A" to 1,
            "B" to 2
        )

        Ion.decodeFromBytes<Map<Int, String>>(
            Ion.encodeToBytes(
                mapOf(
                    1 to "A",
                    2 to "B"
                )
            )
        ) shouldBe mapOf(
            1 to "A",
            2 to "B"
        )
    }

    "lists are serialized and deserialized in binary format" {
        Ion.decodeFromBytes<List<String>>(Ion.encodeToBytes(emptyList<String>())) shouldBe emptyList<String>()
        Ion.decodeFromBytes<List<String>>(Ion.encodeToBytes(listOf("A", "B"))) shouldBe listOf("A", "B")
        Ion.decodeFromBytes<List<Int>>(Ion.encodeToBytes(listOf(1, 2))) shouldBe listOf(1, 2)
    }

    "lists with objects serialized and deserialized in binary format" {
        Ion.decodeFromBytes<List<ListElement>>(
            Ion.encodeToBytes(listOf(ListElement("A"), ListElement("B")))
        ) shouldBe listOf(ListElement("A"), ListElement("B"))
    }
})
