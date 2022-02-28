package hs.aalen.infona.data.request.auth_request

import hs.aalen.infona.data.collections.User

data class RegisterRequest(
    val user: User,
    val password:String
)
