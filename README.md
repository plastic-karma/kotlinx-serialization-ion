# kotlinx-serialization-ion

![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)
[![Build Status](https://api.travis-ci.com/plastic-karma/kotlinx-serialization-ion.svg)](https://travis-ci.com/github/plastic-karma//kotlinx-serialization-ion)

Kotlin Serialization to and from ION.

## Usage
#### Serialize to ION strings
```kotlin
data class DataHolder(val name: String, val id: Int)
// ...
val myData = DataHolder(name = "Fritz", id = 42)
Ion.encodeToString(myData) // == { name : "Fritz, id: 42 }
```

#### Deserialize from ION strings
```kotlin
data class DataHolder(val name: String, val id: Int)
// ...
val myIon = """{ name : "Fritz, id: 42 }"""
Ion.decodeFromString<DataHolder>(myIon) // == DataHolder(name = "Fritz, id = 42)
```

#### Serialize to binary ION
```kotlin
data class DataHolder(val name: String, val id: Int)
// ...
val myData = DataHolder(name = "Fritz", id = 42)
Ion.encodeToBytes(myData) // == binary ION values
```

#### Deserialize from binary ION
```kotlin
data class DataHolder(val name: String, val id: Int)
// ...
val myIon: ByteArray = //... get bytes
Ion.decodeFromBytes<DataHolder>(myIon) // == DataHolder(name = "Fritz, id = 42)
```
