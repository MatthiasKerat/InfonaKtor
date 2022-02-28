package hs.aalen.infona.routes

import hs.aalen.infona.data.databases.getSubjects
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.subjectRoute(){
    route("subject"){
        //authenticate("auth-jwt"){
            get{
                val subjects = getSubjects()
                call.respond(HttpStatusCode.OK,subjects)
            }
        //}
    }
}