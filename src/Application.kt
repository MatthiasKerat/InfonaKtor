package hs.aalen.infona

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.core.json.JsonReadFeature
import hs.aalen.infona.routes.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.http.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders)

    //Logging requests from clients on the console of ktor
    install(CallLogging)

    install(Authentication) {
        val myRealm = environment.config.property("ktor.jwt.realm").getString()
        val secret = environment.config.property("ktor.jwt.secret").getString()
        val issuer = environment.config.property("ktor.jwt.issuer").getString()
        val audience = environment.config.property("ktor.jwt.audience").getString()

        configureAuth(myRealm,secret,issuer,audience)
    }

    install(ContentNegotiation) {
         gson {
            setPrettyPrinting()
            setLenient()
        }
    }

    install(Routing){
        authRoute()
        userRoute()
        subjectRoute()
        topicRoute()
        exerciseRoute()
        imageRoute()
    }
}

private fun Authentication.Configuration.configureAuth(myRealm: String, secret: String, issuer: String, audience: String){
    jwt("auth-jwt"){
        realm = myRealm
        verifier(
            JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build())
        validate { credential ->
            if(credential.payload.expiresAt.time.minus(System.currentTimeMillis())>0){
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }

    }
}

