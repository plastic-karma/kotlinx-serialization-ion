package de.plastickarma.kotlinx.serialization.format.ion.data

import kotlinx.serialization.Serializable

@Serializable
class PrimitiveDataHolder(
    val str: String,
    val c: Char,
    val i: Int,
    val l: Long,
    val f: Float,
    val d: Double,
    val s: Short,
    val b: Byte,
    val bl: Boolean,
    val status: Status
)
