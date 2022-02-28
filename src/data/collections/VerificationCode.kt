package hs.aalen.infona.data.collections

import java.util.*

data class VerificationCode(
    val _id:String = UUID.randomUUID().toString(),
    val email:String,
    val code:String
)