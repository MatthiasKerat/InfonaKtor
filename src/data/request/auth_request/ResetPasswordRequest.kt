package hs.aalen.infona.data.request.auth_request

data class ResetPasswordRequest(
    val email:String,
    val code:String,
    val password:String
)
