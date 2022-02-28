package hs.aalen.infona.data.collections

import java.util.*

data class ResetCode(
    val _id:String = UUID.randomUUID().toString(),
    val email:String,
    val code:String
)