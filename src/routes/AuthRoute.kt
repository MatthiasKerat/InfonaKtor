package hs.aalen.infona.routes

import hs.aalen.infona.common.generateResetPasswordCode
import hs.aalen.infona.common.generateValidateEmailCode
import hs.aalen.infona.common.sendEmail
import hs.aalen.infona.data.*
import hs.aalen.infona.data.collections.User
import hs.aalen.infona.data.request.auth_request.LoginRequest
import hs.aalen.infona.data.request.auth_request.RegisterRequest
import hs.aalen.infona.data.request.auth_request.ResetPasswordRequest
import hs.aalen.infona.data.request.auth_request.ValidateEmailRequest
import hs.aalen.infona.data.response.auth_response.RegisterResponse
import hs.aalen.infona.data.response.auth_response.ForgotPasswordResponse
import hs.aalen.infona.data.response.auth_response.LoginResponse
import hs.aalen.infona.data.response.DefaultResponse
import hs.aalen.infona.data.user_database.checkIfUserExists
import hs.aalen.infona.data.user_database.getUserByEmail
import hs.aalen.infona.security.generateToken
import hs.aalen.infona.security.getHasWithSalt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.delay

fun Route.authRoute(){

    val secret = application.environment.config.property("ktor.jwt.secret").getString()
    val issuer = application.environment.config.property("ktor.jwt.issuer").getString()
    val audience = application.environment.config.property("ktor.jwt.audience").getString()

    route("/auth/login"){
        post {


            val request = try{
                call.receive<LoginRequest>()
            }catch (e: ContentTransformationException){
                print(e.message)
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            //Passwort prüfen und token generieren

            val isPasswordCorrect = checkPasswordForEmail(request.email,request.password)
            if(isPasswordCorrect){
                val token = generateToken(request.email,issuer,audience,secret)
                val user = getUserByEmail(request.email)
                if(user != null){
                    call.respond(HttpStatusCode.OK,
                        LoginResponse(successful = true,message = "Successfully logged in", token = token,validated = user?.validated ?: false, user = user)
                    )
                }else{
                    call.respond(HttpStatusCode.BadRequest)
                }

            }else {
                call.respond(
                    HttpStatusCode.OK,
                    RegisterResponse(successful = false, message = "The Email or password is incorrect")
                )
            }

        }
    }
    route("/auth/register"){
        post{
            val request = try{
                call.receive<RegisterRequest>()
            }catch (e: ContentTransformationException){
                print(e.message)
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val user = request.user.copy(password = getHasWithSalt(request.password))

            val userExists = checkIfUserExists(user.email)
            if(!userExists){
                val token = generateToken(user.email,issuer,audience,secret)
                if(registerUser(user)){
                    sendEmail(toEmail = user.email,subject = "Validate your email address",message = generateValidateEmailCode(6, email = user.email))
                    call.respond(HttpStatusCode.OK, RegisterResponse(true,"Successfully created account!",token))
                }else{
                    call.respond(HttpStatusCode.OK, RegisterResponse(false, "Could not create user"))
                }
            }else{
                call.respond(HttpStatusCode.OK, RegisterResponse(false, "An user with this email already exists."))
            }
        }
    }

    route("/auth/check_user/{email}"){
        get {
            val email = call.parameters["email"]
            if(email != null){
                val userExists = checkIfUserExists(email)
                if(userExists){
                    call.respond(HttpStatusCode.OK, ForgotPasswordResponse(true))
                    sendEmail(toEmail = email, subject = "Reset password code",message = generateResetPasswordCode(6, email = email))
                }else{
                    call.respond(HttpStatusCode.OK, ForgotPasswordResponse(false))
                }
            }else{
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }

    route("/auth/reset_password"){
        post {
            val request = try{

                call.receive<ResetPasswordRequest>()
            }catch (e: ContentTransformationException){
                print(e.message)
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if(!verifyResetCode(request.code,request.email)){
                call.respond(HttpStatusCode.OK,DefaultResponse(false,"Invalid reset code"))
            }else{
                if(resetPasswordForUser(request.email, getHasWithSalt(request.password))){
                    call.respond(HttpStatusCode.OK,DefaultResponse(true,null))
                }else{
                    call.respond(HttpStatusCode.OK,DefaultResponse(false,"Database error. Could not reset password"))
                }
            }
       }
    }

    route("/auth/validate_email"){
        authenticate ("auth-jwt") {
            post{
                val request = try {
                    call.receive<ValidateEmailRequest>()
                }catch (e: ContentTransformationException){
                    print(e.message)
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(validateCode(request.code,request.email)){
                    call.respond(HttpStatusCode.OK,DefaultResponse(true,null))
                }else{
                    call.respond(HttpStatusCode.OK,DefaultResponse(false,"Wrong validation code"))
                }
            }
        }
    }

    route("auth/request_verification_code/{email}"){
        get {
            val email = call.parameters["email"]
            if(email != null){
                val userExists = checkIfUserExists(email)
                if(userExists){
                    val code = getVerificationCode(email)
                    call.respond(HttpStatusCode.OK, DefaultResponse(true,null))
                    sendEmail(toEmail = email, subject = "Validate your email address",message = code)
                }else{
                    call.respond(HttpStatusCode.OK, DefaultResponse(false,"Email address is not registered yet"))
                }
            }else{
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}