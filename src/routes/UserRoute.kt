package hs.aalen.infona.routes

import hs.aalen.infona.data.collections.User
import hs.aalen.infona.data.databases.getSubjects
import hs.aalen.infona.data.response.DefaultResponse
import hs.aalen.infona.data.user_database.getUsers
import hs.aalen.infona.data.user_database.updateUserData
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userRoute(){
    route("user/update"){
        //authenticate("auth-jwt"){
            post{
                val request = try {
                    call.receive<User>()
                } catch(e : ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                if(updateUserData(request)){
                    call.respond(
                        HttpStatusCode.OK,
                        DefaultResponse(true, "User data successfully updated")
                    )
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        //}
    }
    route("user"){
        //authenticate("auth-jwt"){
            get{
                val users = getUsers()
                call.respond(HttpStatusCode.OK,users)
            }
        //}
    }
}