package de.plastickarma.kotlinx.serialization.format.ion

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
class ContainerTypesIonEncoderTest : FreeSpec({
    "lists are serialized" {
        Ion.encodeToString(emptyList<String>()) shouldBeIon "[]"
        Ion.encodeToString(listOf("A", "B")) shouldBeIon "[\"A\",\"B\"]"
        Ion.encodeToString(listOf(1, 2)) shouldBeIon "[1,2]"
    }

    "lists with objects are serialized" {
        @Serializable
        data class InsideList(val id: String)
        Ion.encodeToString(listOf(InsideList("A"), InsideList("B"))) shouldBe "[{id:\"A\"},{id:\"B\"}]"
    }

    "maps are serialized" {
        Ion.encodeToString(emptyMap<String, String>()) shouldBeIon "{}"
        Ion.encodeToString(
            mapOf(
                "A" to 1,
                "B" to 2
            )
        ) shouldBeIon """
            {A:1,B:2}
        """
        Ion.encodeToString(
            mapOf(
                1 to "A",
                2 to "B"
            )
        ) shouldBeIon """
            {'1':"A",'2':"B"}
        """
    }

    "nested containers are getting serliazed" {
        Ion.encodeToString(
            listOf(
                mapOf("A" to 1, "B" to 2),
                mapOf("C" to 3, "D" to 4)
            )
        ) shouldBeIon """
            [{A:1,B:2},{C:3,D:4}]
        """

        Ion.encodeToString(
            mapOf(
                "key1" to listOf(1, 2, 3),
                "key2" to listOf(4, 5, 6),
            )
        ) shouldBeIon """
            {key1:[1,2,3],key2:[4,5,6]}
        """
    }
})
