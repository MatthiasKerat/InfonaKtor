package hs.aalen.infona.routes

import hs.aalen.infona.data.databases.getAllExercises
import hs.aalen.infona.data.databases.getExercises
import hs.aalen.infona.data.databases.getSubjects
import hs.aalen.infona.data.databases.getTopics
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.exerciseRoute(){

    route("exercise/{topic_id}"){
        //authenticate("auth-jwt"){
        get{
            val topicId = call.parameters["topic_id"]
            if(topicId != null){
                val exercises = getExercises(topicId)
                call.respond(HttpStatusCode.OK,exercises)
            }else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        //}
    }

    route("exercise"){
        //authenticate("auth-jwt"){
        get{
            val exercises = getAllExercises()
            call.respond(HttpStatusCode.OK,exercises)
        }
        //}
    }
}