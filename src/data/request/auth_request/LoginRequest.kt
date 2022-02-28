package hs.aalen.infona.data.request.auth_request



data class LoginRequest(

    val email:String,

    val password:String,

    val stay_logged_in:Boolean
)
