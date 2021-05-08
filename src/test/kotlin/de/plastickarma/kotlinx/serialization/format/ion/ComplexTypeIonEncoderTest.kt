package de.plastickarma.kotlinx.serialization.format.ion

import de.plastickarma.kotlinx.serialization.format.ion.data.NestedDataHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.NestedDataHolderWithOptional
import de.plastickarma.kotlinx.serialization.format.ion.data.PrimitiveDataHolder
import de.plastickarma.kotlinx.serialization.format.ion.data.Status
import io.kotest.core.spec.style.FreeSpec
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ComplexTypeIonEncoderTest : FreeSpec({

    "complex types are serialized" {
        Ion.encodeToString(
            PrimitiveDataHolder(
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
        ) shouldBeIon """
            {str:"Hello",c:"c",i:1,l:100000000001,f:1234.530029296875e0,d:-0.12312313e0,s:12,b:100,bl:true, status: "UPDATED"}
        """
    }

    "nested complex types are serialized" {
        Ion.encodeToString(
            NestedDataHolder(
                primitiveDataHolder = PrimitiveDataHolder(str = "Hello", 'c', 1, 100000000001, 1234.53f, -0.12312313, 12, 100, true, status = Status.UPDATED),
                listOfPrimitiveDataHolder = listOf(
                    PrimitiveDataHolder(str = "Hello1", 'c', 1, 100000000001, 1234.51f, -0.12312313, 12, 100, true, status = Status.UPDATED),
                    PrimitiveDataHolder(str = "Hello2", 'c', 2, 200000000001, 1234.52f, -0.22312313, 22, 101, false, status = Status.DELETED),
                    PrimitiveDataHolder(str = "Hello3", 'c', 3, 300000000001, 1234.53f, -0.32312313, 32, 102, true, status = Status.UPDATED),
                ),
                mapOfPrimitiveDataHolder = mapOf(
                    "key1" to PrimitiveDataHolder(str = "HelloKey1", 'c', 1, 100000000001, 1234.51f, -0.12312313, 12, 100, true, status = Status.UPDATED),
                    "key2" to PrimitiveDataHolder(str = "HelloKey2", 'c', 1, 100000000001, 1234.51f, -0.12312313, 12, 100, true, status = Status.DELETED),
                )
            )
        ) shouldBeIon """
            {
                primitiveDataHolder:{str:"Hello",c:"c",i:1,l:100000000001,f:1234.530029296875e0,d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                listOfPrimitiveDataHolder:[
                    {str:"Hello1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                    {str:"Hello2",c:"c",i:2,l:200000000001,f:1234.52001953125e0,d:-0.22312313e0,s:22,b:101,bl:false,status:"DELETED"},
                    {str:"Hello3",c:"c",i:3,l:300000000001,f:1234.530029296875e0,d:-0.32312313e0,s:32,b:102,bl:true,status:"UPDATED"}
                ],
                mapOfPrimitiveDataHolder:{
                    key1:{str:"HelloKey1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                    key2:{str:"HelloKey2",c:"c",i:1,l:100000000001,f:1234.510009765625e0,d:-0.12312313e0,s:12,b:100,bl:true,status:"DELETED"}
                }
            }
        """
    }

    "complex types with optional properties are serialized" {
        Ion.encodeToString(
            NestedDataHolderWithOptional(
                primitiveDataHolder = null,
                listOfPrimitiveDataHolder = null,
                mapOfPrimitiveDataHolder = null
            )
        ) shouldBeIon """{primitiveDataHolder:null,listOfPrimitiveDataHolder:null,mapOfPrimitiveDataHolder:null}"""

        Ion.encodeToString(
            NestedDataHolderWithOptional(
                primitiveDataHolder = null,
                listOfPrimitiveDataHolder = listOf(
                    null,
                    PrimitiveDataHolder(str = "Hello1", 'c', 1, 100000000001, 1234.51f, -0.12312313, 12, 100, true, status = Status.UPDATED),
                    null
                ),
                mapOfPrimitiveDataHolder = null
            )
        ) shouldBeIon """
            {
                primitiveDataHolder:null,
                listOfPrimitiveDataHolder:[
                    null,
                    {str:"Hello1",c:"c",i:1,l:100000000001,f:1234.510009765625e0,d:-0.12312313e0,s:12,b:100,bl:true,status:"UPDATED"},
                    null
                ],
                mapOfPrimitiveDataHolder:null
                }
        """
    }
})
