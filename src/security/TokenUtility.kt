package hs.aalen.infona.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateToken(email: String, issuer:String, audience:String, secret: String ) =
    JWT.create()
        .withIssuer(issuer)
        .withClaim("email", email)
        .withAudience(audience)
        .withExpiresAt(Date(System.currentTimeMillis()+9999990000))
        .sign(Algorithm.HMAC256(secret))