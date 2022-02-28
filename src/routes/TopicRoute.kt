package hs.aalen.infona.routes

import hs.aalen.infona.data.databases.getTopics
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.topicRoute(){
    route("topic/{subject_id}"){
        //authenticate("auth-jwt"){
        get{
            val subjectId = call.parameters["subject_id"]
            if(subjectId != null){
                val topics = getTopics(subjectId)
                call.respond(HttpStatusCode.OK,topics)
            }else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        //}
    }
}