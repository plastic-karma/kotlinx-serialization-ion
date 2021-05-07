package de.plastickarma.kotlinx.serialization.format.ion

import com.amazon.ion.system.IonReaderBuilder
import com.amazon.ion.system.IonTextWriterBuilder
import io.kotest.matchers.shouldBe

infix fun String.shouldBeIon(expectedIon: String) {
    this.flatten() shouldBe expectedIon.flatten()
    this.isIonFormat() shouldBe true
}

private fun String.flatten(): String {
    return this.trimIndent().replace("\n", "").replace("\\s".toRegex(), "")
}

private fun String.isIonFormat(): Boolean {
    return try {
        IonTextWriterBuilder.standard().build(StringBuilder()).use { writer ->
            writer.writeValues(IonReaderBuilder.standard().build(this))
            true
        }
    } catch (e: Throwable) {
        false
    }
}
