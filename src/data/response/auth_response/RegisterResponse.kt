package hs.aalen.infona.data.response.auth_response


data class RegisterResponse(
    val successful: Boolean,
    val message: String,
    val token: String? = null
)
