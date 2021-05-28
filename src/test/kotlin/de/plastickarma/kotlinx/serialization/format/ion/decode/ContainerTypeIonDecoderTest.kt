package de.plastickarma.kotlinx.serialization.format.ion.decode

import de.plastickarma.kotlinx.serialization.format.ion.Ion
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ContainerTypeIonDecoderTest : FreeSpec({
    "lists are deserialized" {
        Ion.decodeFromString<List<String>>("[]") shouldBe emptyList()
        Ion.decodeFromString<List<String>>("[\"A\",\"B\"]") shouldBe listOf("A", "B")
        Ion.decodeFromString<List<Int>>("[1,2]") shouldBe listOf(1, 2)
    }

    "nested lists are deserialized" {
        val decodeFromString = Ion.decodeFromString<List<List<Int>>>("[[1,2],[3,4],[5,6]]")
        decodeFromString shouldBe listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6))
    }

    "maps are deserialized" {
        Ion.decodeFromString<Map<String, Int>>("""{}""") shouldBe emptyMap()
        Ion.decodeFromString<Map<String, Int>>(
            """
            { A: 1, B: 2}
            """.trimIndent()
        ) shouldBe mapOf("A" to 1, "B" to 2)
        Ion.decodeFromString<Map<Int, String>>(
            """
            {'1':"A",'2':"B",'3':"C"}
            """.trimIndent()
        ) shouldBe mapOf(1 to "A", 2 to "B", 3 to "C")
    }

    "nested maps are deserialized" {
        Ion.decodeFromString<Map<String, Map<Int, String>>>(
            """
            { 
                X: {'1':"A",'2':"B",'3':"C"}, 
                Y: {'4':"D",'5':"E",'6':"F"}
            }
            """.trimIndent()
        ) shouldBe mapOf(
            "X" to mapOf(1 to "A", 2 to "B", 3 to "C"),
            "Y" to mapOf(4 to "D", 5 to "E", 6 to "F")
        )
    }

    "maps of lists are deserialized" {
        Ion.decodeFromString<Map<String, List<Int>>>(
            """
            {
                X: [1,2,3],
                Y: [4,5,6]
            }
            """.trimIndent()
        ) shouldBe mapOf(
            "X" to listOf(1, 2, 3),
            "Y" to listOf(4, 5, 6)
        )
    }
})
