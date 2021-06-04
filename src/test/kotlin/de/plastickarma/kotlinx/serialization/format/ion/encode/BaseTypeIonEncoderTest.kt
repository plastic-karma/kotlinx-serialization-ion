package de.plastickarma.kotlinx.serialization.format.ion.encode

import de.plastickarma.kotlinx.serialization.format.ion.Ion
import de.plastickarma.kotlinx.serialization.format.ion.shouldBeIon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class BaseTypeIonEncoderTest : FreeSpec({
    "strings are serialized" - {
        Ion.encodeToString("Hello") shouldBeIon "\"Hello\""
        Ion.encodeToString("") shouldBeIon "\"\""
    }

    "ints are serialized" {
        Ion.encodeToString(1) shouldBeIon "1"
        Ion.encodeToString(-1) shouldBeIon "-1"
        Ion.encodeToString(0) shouldBeIon "0"
        Ion.encodeToString(100000000) shouldBe "100000000"
    }

    "floats are serialized" {
        Ion.encodeToString(1.0f) shouldBeIon "1e0"
        Ion.encodeToString(-1f) shouldBeIon "-1e0"
        Ion.encodeToString(0.0f) shouldBeIon "0e0"
        Ion.encodeToString(100000000f) shouldBeIon "1.0E8"
        Ion.encodeToString(0.567) shouldBeIon "0.567e0"
    }

    "doubles are serialized" {
        Ion.encodeToString(1.0) shouldBeIon "1e0"
        Ion.encodeToString(-1.0) shouldBeIon "-1e0"
        Ion.encodeToString(0.0) shouldBeIon "0e0"
        Ion.encodeToString(100000000.0) shouldBeIon "1.0E8"
        Ion.encodeToString(0.567) shouldBeIon "0.567e0"
    }

    "null is serialized" {
        Ion.encodeToString(null as String?) shouldBeIon "null"
        Ion.encodeToString(null as Int?) shouldBeIon "null"
        Ion.encodeToString(null as Double?) shouldBeIon "null"
        Ion.encodeToString(null as Float?) shouldBeIon "null"
    }
})
