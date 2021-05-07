package de.plastickarma.kotlinx.serialization.format.ion.data

import kotlinx.serialization.Serializable

@Serializable
data class NestedDataHolder(
    val primitiveDataHolder: PrimitiveDataHolder,
    val listOfPrimitiveDataHolder: List<PrimitiveDataHolder>,
    val mapOfPrimitiveDataHolder: Map<String, PrimitiveDataHolder>
)
