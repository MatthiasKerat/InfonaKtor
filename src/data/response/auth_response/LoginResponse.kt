package hs.aalen.infona.data.response.auth_response

import hs.aalen.infona.data.collections.User

data class LoginResponse (
    val successful: Boolean,
    val message: String,
    val validated:Boolean,
    val user:User,
    val token: String? = null
)