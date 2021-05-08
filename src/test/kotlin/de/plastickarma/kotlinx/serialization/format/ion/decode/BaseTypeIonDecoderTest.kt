package de.plastickarma.kotlinx.serialization.format.ion.decode

import de.plastickarma.kotlinx.serialization.format.ion.Ion
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class BaseTypeIonDecoderTest : FreeSpec({
    "strings are deserialized" - {
        Ion.decodeFromString<String>("\"Hello\"") shouldBe "Hello"
        Ion.decodeFromString<String>("\"\"") shouldBe ""
    }

    "ints are deserialized" {
        Ion.decodeFromString<Int>("1") shouldBe 1
        Ion.decodeFromString<Int>("-1") shouldBe -1
        Ion.decodeFromString<Int>("0") shouldBe 0
        Ion.decodeFromString<Int>("100000000") shouldBe 100000000
    }

    "floats are deserialized" {
        Ion.decodeFromString<Float>("-1e0") shouldBe -1.0f
        Ion.decodeFromString<Float>("1e0") shouldBe 1.0f
        Ion.decodeFromString<Float>("0e0") shouldBe 0
        Ion.decodeFromString<Float>("1.0E8") shouldBe 1.0E8f
        Ion.decodeFromString<Float>("0.567e0") shouldBe 0.567f
    }

    "doubles are deserialized" {
        Ion.decodeFromString<Double>("-1e0") shouldBe -1.0
        Ion.decodeFromString<Double>("1e0") shouldBe 1.0
        Ion.decodeFromString<Double>("0e0") shouldBe 0
        Ion.decodeFromString<Double>("1.0E8") shouldBe 1.0E8
        Ion.decodeFromString<Double>("0.567e0") shouldBe 0.567
    }

    "null is deserialized" {
        Ion.decodeFromString<String?>("null") shouldBe null
        Ion.decodeFromString<Int?>("null") shouldBe null
        Ion.decodeFromString<Double?>("null") shouldBe null
        Ion.decodeFromString<Float?>("null") shouldBe null
    }
})
