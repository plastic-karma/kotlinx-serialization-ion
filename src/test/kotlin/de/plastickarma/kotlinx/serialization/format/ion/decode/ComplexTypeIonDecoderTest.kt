package de.plastickarma.kotlinx.serialization.format.ion.decode

import de.plastickarma.kotlinx.serialization.format.ion.Ion
import de.plastickarma.kotlinx.serialization.format.ion.data.InlineElementHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.MyInt
import de.plastickarma.kotlinx.serialization.format.ion.data.NestedDataHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.NestedDataHolderWithOptional
import de.plastickarma.kotlinx.serialization.format.ion.data.PrimitiveDataHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.Status
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ComplexTypeIonDecoderTest : FreeSpec({

    "complex types are deserialized" {
        Ion.decodeFromString<PrimitiveDataHolder>(
            """
            {str:"Hello",c:"c",i:1,l:100000000001,f:1234.530029296875e0,
            d:-0.12312313e0,s:12,b:100,bl:true, status: "UPDATED"}
        """

        ) shouldBe PrimitiveDataHolder(
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

        Ion.decodeFromString<PrimitiveDataHolder>(
            """
            {str:"Hello",c:"c",i:1,l:100000000001,f:1234.530029296875e0,
            d:-0.12312313e0,s:12,b:100,bl:false, status: "DELETED"}
        """

        ) shouldBe PrimitiveDataHolder(
            str = "Hello",
            'c',
            1,
            100000000001,
            1234.53f,
            -0.12312313,
            12,
            100,
            false,
            Status.DELETED
        )
    }

    "nested complex types are deserialized" {
        Ion.decodeFromString<NestedDataHolder>(
            """
            {
                primitiveDataHolder:{str:"Hello",c:"c",i:1,l:100000000001,f:1234.530029296875e0,
                d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                listOfPrimitiveDataHolder:[
                    {str:"Hello1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,
                    d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                    {str:"Hello2",c:"c",i:2,l:200000000001,f:1234.52001953125e0,
                    d:-0.22312313e0,s:22,b:101,bl:false,status:"DELETED"},
                    {str:"Hello3",c:"c",i:3,l:300000000001,f:1234.530029296875e0,
                    d:-0.32312313e0,s:32,b:102,bl:true,status:"UPDATED"}
                ],
                mapOfPrimitiveDataHolder:{
                    key1:{str:"HelloKey1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,
                    d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                    key2:{str:"HelloKey2",c:"c",i:1,l:100000000001,f:1234.510009765625e0,
                    d:-0.12312313e0,s:12,b:100,bl:true,status:"DELETED"}
                }
            }
        """
        ) shouldBe NestedDataHolder(
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
    }

    "complex types with optional properties are deserialized" {
        Ion.decodeFromString<NestedDataHolderWithOptional>(
            """
            {primitiveDataHolder:null,listOfPrimitiveDataHolder:null,mapOfPrimitiveDataHolder:null}
            """
        ) shouldBe NestedDataHolderWithOptional(
            primitiveDataHolder = null,
            listOfPrimitiveDataHolder = null,
            mapOfPrimitiveDataHolder = null
        )

        Ion.decodeFromString<NestedDataHolderWithOptional>(
            """
            {
                primitiveDataHolder:null,
                listOfPrimitiveDataHolder:[
                    null,
                    {str:"Hello1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,
                    d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                    null
                ],
                mapOfPrimitiveDataHolder:null
                }
        """
        ) shouldBe NestedDataHolderWithOptional(
            primitiveDataHolder = null,
            listOfPrimitiveDataHolder = listOf(
                null,
                PrimitiveDataHolder(
                    str = "Hello1", 'c', 1, 100000000001, 1234.51f,
                    -0.12312313, 12, 100, true, status = Status.UPDATED
                ),
                null
            ),
            mapOfPrimitiveDataHolder = null
        )
    }

    "inline classes are deserialized" {
        Ion.decodeFromString<MyInt>("42") shouldBe MyInt(42)
    }

    "nested inline classes are deserialized" {
        Ion.decodeFromString<InlineElementHolder>(
            """{ myInt:43,id:"hello"}"""
        ) shouldBe InlineElementHolder(MyInt(43), "hello")
    }

    "ion strings with symbol tables are deserialized" {
        Ion.decodeFromString<NestedDataHolderWithOptional>(
            """
        ${'$'}ion_1_0 
        ${'$'}ion_symbol_table::
            {
              symbols:[ "primitiveDataHolder", "mapOfPrimitiveDataHolder", "status" ]
            }
        {
            ${'$'}10:null,
            listOfPrimitiveDataHolder:[
                null,
                {str:"Hello1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,
                d:-0.12312313e0,s:12,b:100,bl:true,${'$'}12:"UPDATED"},
                null
            ],
            ${'$'}11:null
        }
        """
        ) shouldBe NestedDataHolderWithOptional(
            primitiveDataHolder = null,
            listOfPrimitiveDataHolder = listOf(
                null,
                PrimitiveDataHolder(
                    str = "Hello1", 'c', 1, 100000000001, 1234.51f,
                    -0.12312313, 12, 100, true, status = Status.UPDATED
                ),
                null
            ),
            mapOfPrimitiveDataHolder = null
        )
    }
})
