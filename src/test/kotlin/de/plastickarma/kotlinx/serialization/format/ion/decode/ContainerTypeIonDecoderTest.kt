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
        Ion.decodeFromString<List<List<Int>>>("[[1,2],[3,4],[5,6]]") shouldBe listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6))
    }
})
