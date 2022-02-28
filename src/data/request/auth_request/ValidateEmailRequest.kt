package hs.aalen.infona.data.request.auth_request

data class ValidateEmailRequest(
    val email:String,
    val code:String
)