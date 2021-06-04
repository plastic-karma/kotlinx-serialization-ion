# kotlinx-serialization-ion

![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)
[![Build Status](https://api.travis-ci.com/plastic-karma/kotlinx-serialization-ion.svg)](https://travis-ci.com/github/plastic-karma//kotlinx-serialization-ion)

Kotlin Serialization to and from ION.

## Usage
#### Serialize to ION
```kotlin
data class DataHolder(val name: String, val id: Int)
// ...
val myData = DataHolder(name = "Fritz", id = 42)
Ion.encodeToString(myData) // == { name : "Fritz, id: 42 }
```

#### Deserialize from ION
```kotlin
data class DataHolder(val name: String, val id: Int)
// ...
val myIon = """{ name : "Fritz, id: 42 }"""
Ion.decodeFromString<DataHolder>(myIon) // == DataHolder(name = "Fritz, id = 42)
```
